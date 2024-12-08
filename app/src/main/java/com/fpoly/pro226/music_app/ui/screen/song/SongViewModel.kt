package com.fpoly.pro226.music_app.ui.screen.song

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.data.repositories.FMusicRepository
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.comment.CommentBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.comment.CommentResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.favorite.FavoriteBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.favorite.FavoriteResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.ItemPlaylistBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.ItemPlaylistResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlayListResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.updateCountPlaylist
import com.fpoly.pro226.music_app.data.source.network.models.Album
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


data class SongUiState(
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val currentSong: Track? = null,
    val album: Album? = null,
    val playListResponse: PlayListResponse? = null,
    val commentResponse: CommentResponse? = null,
    val isFavorite: Boolean = false,
    val favoriteResponse: FavoriteResponse? = null
)

class SongViewModel(
    private val deezerRepository: DeezerRepository,
    private val fMusicRepository: FMusicRepository,
    private val userId: String = "",
) : ViewModel() {

    companion object {

        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}
        val MY_REPOSITORY_KEY_2 = object : CreationExtras.Key<DeezerRepository> {}

        fun provideFactory(userId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fMusicRepo = this[MY_REPOSITORY_KEY] as FMusicRepository
                val deezerRepo = this[MY_REPOSITORY_KEY_2] as DeezerRepository

                SongViewModel(
                    fMusicRepository = fMusicRepo,
                    deezerRepository = deezerRepo,
                    userId = userId
                )
            }
        }
    }

    private var fetchFav: Job? = null
    private var fetchComment: Job? = null
    private var addFavJob: Job? = null
    private var fetchPlaylists: Job? = null

    var songUiState by mutableStateOf(SongUiState())
        private set

    val fetchAlbumEvent = MutableStateFlow<Album?>(null)

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    init {
        getFavorites()
    }

    suspend fun showToast(message: String) {
        _toastEvent.emit(message)
    }


    fun addItemToPlaylist(itemPlaylistBody: ItemPlaylistBody) {
        viewModelScope.launch {
            try {
                songUiState = songUiState.copy(isLoading = true)
                val response = fMusicRepository.addItemToPlaylist(itemPlaylistBody)
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        showToast(res.message)
                        songUiState = songUiState.copy(
                            isLoading = false,
                            playListResponse = songUiState.playListResponse?.updateCountPlaylist(
                                count = res.data.size,
                                id = itemPlaylistBody.id_playlist
                            )
                        )
                    }
                } else if (response.code() == 400) {
                    response.errorBody()?.let { res ->
                        try {
                            val errorResponse =
                                Gson().fromJson(res.string(), ItemPlaylistResponse::class.java)
                            showToast(errorResponse.message)
                            songUiState = songUiState.copy(
                                isLoading = false,
                            )
                        } catch (e: Exception) {
                            songUiState = songUiState.copy(isLoading = false)
                        }

                    }
                }

            } catch (e: Exception) {
                songUiState = songUiState.copy(isLoading = false)
            }
        }
    }

    fun getAllPlaylist() {
        fetchPlaylists?.cancel()
        fetchPlaylists = viewModelScope.launch {
            try {
                songUiState = songUiState.copy(isLoading = true)
                val response = fMusicRepository.getPlaylist(userId)
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        songUiState = songUiState.copy(
                            playListResponse = res,
                            isLoading = false,
                        )
                    }
                }
            } catch (e: Exception) {
                songUiState = songUiState.copy(isLoading = false)
            } finally {
                fetchPlaylists = null
            }
        }
    }

    fun getAllComment(trackId: String) {
        fetchComment?.cancel()
        fetchComment = viewModelScope.launch {
            try {
                songUiState = songUiState.copy(isLoading = true)
                val response = fMusicRepository.getComments(trackId)
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        songUiState = songUiState.copy(
                            isLoading = false,
                            commentResponse = res
                        )
                    }
                } else {
                    songUiState = songUiState.copy(
                        isLoading = false,
                        commentResponse = songUiState.commentResponse?.copy(data = listOf())
                    )
                }
            } catch (e: Exception) {
                songUiState = songUiState.copy(isLoading = false)
            } finally {
                fetchComment = null
            }
        }
    }

    fun addComment(commentBody: CommentBody) {
        viewModelScope.launch {
            try {
                songUiState = songUiState.copy(isLoading = true)
                val response = fMusicRepository.addComment(commentBody.copy(id_user = userId))
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        songUiState = songUiState.copy(
                            isLoading = false,
                            commentResponse = res
                        )
                    }
                } else if (response.code() == 400) {
                    response.errorBody()?.let { res ->
                        try {
                            val errorResponse =
                                Gson().fromJson(res.string(), CommentResponse::class.java)
                            showToast(errorResponse.message)
                        } catch (e: Exception) {
                            songUiState = songUiState.copy(isLoading = false)
                        }

                    }
                } else {
                    songUiState = songUiState.copy(isLoading = false)
                }
            } catch (e: Exception) {
                songUiState = songUiState.copy(isLoading = false)
            }
        }
    }

    fun addFavorite(favoriteBody: FavoriteBody) {
        viewModelScope.launch {
            try {
                songUiState = songUiState.copy(isLoading = true, isFavorite = true)
                val response = fMusicRepository.addFavorite(favoriteBody.copy(id_user = userId))
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        songUiState = songUiState.copy(
                            isLoading = false,
                            isFavorite = true
                        )
                    }
                } else {
                    songUiState = songUiState.copy(isLoading = false)
                }
            } catch (e: Exception) {
                songUiState = songUiState.copy(isLoading = false)
            }
        }
    }

    private fun getFavorites() {
        fetchFav?.cancel()
        fetchFav = viewModelScope.launch {
            try {
                songUiState = songUiState.copy(isLoading = true)
                val response = fMusicRepository.getFavorite(userId)
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        songUiState = songUiState.copy(favoriteResponse = res, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                songUiState = songUiState.copy(isLoading = false)
            } finally {
                fetchFav = null
            }
        }
    }

    fun updateFavorite(trackId: String) {
        val favorites = fMusicRepository.currentFavorites
        val value = favorites.firstOrNull { it.id_track == trackId } != null
        songUiState = songUiState.copy(isFavorite = value)
    }

    fun deleteFavorite(trackId: String) {
        viewModelScope.launch {
            try {
                songUiState = songUiState.copy(isLoading = true, isFavorite = false)
                val favorites = fMusicRepository.currentFavorites
                val favorite = favorites.firstOrNull { it.id_track == trackId }
                favorite?.let { fv ->
                    val response = fMusicRepository.deleteFavorite(fv._id)
                    if (response.isSuccessful) {
                        response.body()?.let { res ->
                            fMusicRepository.removeFavoriteLocal(fv.id_track)
                            songUiState = songUiState.copy(
                                isLoading = false,
                                isFavorite = false
                            )
                        }
                    } else {
                        songUiState = songUiState.copy(isLoading = false)
                    }
                }

            } catch (e: Exception) {
                songUiState = songUiState.copy(isLoading = false)
            }
        }
    }

    fun deleteComment(commentId: String) {
        viewModelScope.launch {
            try {
                songUiState = songUiState.copy(isLoading = true)
                val response = fMusicRepository.deleteComment(commentId)
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        songUiState = songUiState.copy(
                            isLoading = false,
                            commentResponse = res
                        )
                    }
                } else {
                    songUiState = songUiState.copy(isLoading = false)
                }
            } catch (e: Exception) {
                songUiState = songUiState.copy(isLoading = false)
            }
        }
    }
}
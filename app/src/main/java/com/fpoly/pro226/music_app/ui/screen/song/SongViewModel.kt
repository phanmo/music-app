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
    val commentResponse: CommentResponse? = null
)

class SongViewModel(
    private val deezerRepository: DeezerRepository,
    private val fMusicRepository: FMusicRepository
) : ViewModel() {

    companion object {

        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}
        val MY_REPOSITORY_KEY_2 = object : CreationExtras.Key<DeezerRepository> {}

        fun provideFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fMusicRepo = this[MY_REPOSITORY_KEY] as FMusicRepository
                val deezerRepo = this[MY_REPOSITORY_KEY_2] as DeezerRepository

                SongViewModel(
                    fMusicRepository = fMusicRepo,
                    deezerRepository = deezerRepo
                )
            }
        }
    }

    private var fetchSong: Job? = null
    private var fetchComment: Job? = null
    private var fetchAlbum: Job? = null
    private var fetchPlaylists: Job? = null

    private var _userId: String = ""

    var songUiState by mutableStateOf(SongUiState())
        private set

    val fetchAlbumEvent = MutableStateFlow<Album?>(null)

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    suspend fun showToast(message: String) {
        _toastEvent.emit(message)
    }


    private fun setCurrentUserId(id: String) {
        _userId = id
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

    fun getAllPlaylist(idUser: String) {
        setCurrentUserId(idUser)
        fetchPlaylists?.cancel()
        fetchPlaylists = viewModelScope.launch {
            try {
                songUiState = songUiState.copy(isLoading = true)
                val response = fMusicRepository.getPlaylist(idUser)
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
                val response = fMusicRepository.addComment(commentBody.copy(id_user = _userId))
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
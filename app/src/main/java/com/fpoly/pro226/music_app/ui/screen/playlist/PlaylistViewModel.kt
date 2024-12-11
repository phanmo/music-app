package com.fpoly.pro226.music_app.ui.screen.playlist

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
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.favorite.FavoriteBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.DeleteItemBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.toTrack
import com.fpoly.pro226.music_app.data.source.network.models.Playlist
import com.fpoly.pro226.music_app.data.source.network.models.Tracks
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

data class PlaylistUiState(
    val isLoading: Boolean = false,
    val tracks: Tracks? = null,
    val playlist: Playlist? = null,
)

class PlaylistViewModel(
    private val deezerRepository: DeezerRepository,
    private val fMusicRepository: FMusicRepository,
    private val idPlaylist: String,
    private val isMyPlaylist: Boolean = false,
    private val userId: String,

    ) : ViewModel() {
    companion object {

        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}
        val MY_REPOSITORY_KEY_2 = object : CreationExtras.Key<DeezerRepository> {}

        fun provideFactory(
            idPlaylist: String,
            isMyPlaylist: Boolean = false,
            userId: String
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fMusicRepo = this[MY_REPOSITORY_KEY] as FMusicRepository
                val deezerRepo = this[MY_REPOSITORY_KEY_2] as DeezerRepository

                PlaylistViewModel(
                    deezerRepository = deezerRepo,
                    idPlaylist = idPlaylist,
                    fMusicRepository = fMusicRepo,
                    isMyPlaylist = isMyPlaylist,
                    userId = userId
                )
            }
        }
    }

    var playlistUiState by mutableStateOf(PlaylistUiState())
        private set

    private var fetchTracks: Job? = null
    private var fetchMyTracks: Job? = null

    init {
        if (!isMyPlaylist) {
            getPlaylistById(idPlaylist)
        } else {
            getMyPlaylistById(idPlaylist)
        }
    }

    private fun getPlaylistById(id: String) {
        fetchTracks?.cancel()
        fetchTracks = viewModelScope.launch {
            try {
                playlistUiState = playlistUiState.copy(isLoading = true)
                val response = deezerRepository.getPlaylistById(id)
                if (response.isSuccessful) {
                    response.body()?.let { playlist ->
                        playlistUiState = playlistUiState.copy(
                            tracks = playlist.tracks,
                            isLoading = false,
                            playlist = playlist
                        )
                    }
                } else {
                    playlistUiState = playlistUiState.copy(isLoading = false)
                }
            } catch (e: Exception) {
                playlistUiState = playlistUiState.copy(isLoading = false)
            } finally {
                fetchTracks = null
            }
        }
    }

    fun deleteItemPlaylist(deleteItemBody: DeleteItemBody) {
        viewModelScope.launch {
            try {
                playlistUiState = playlistUiState.copy(isLoading = true)
                val response = fMusicRepository.deleteItemInPlaylist(deleteItemBody)
                if (response.isSuccessful) {
                    response.body()?.let { playlist ->
                        playlistUiState = playlistUiState.copy(
                            tracks = Tracks(data = playlist.data.map { itemPlaylist ->
                                itemPlaylist.toTrack()
                            }),
                            isLoading = false,
                            playlist = Playlist(
                                title = playlist.playlistName ?: "",
                                description = "${playlist.data.size} Songs"
                            )
                        )
                    }
                } else {
                    playlistUiState = playlistUiState.copy(isLoading = false)
                }
            } catch (e: Exception) {
                playlistUiState = playlistUiState.copy(isLoading = false)
            }
        }
    }


    private fun getMyPlaylistById(id: String) {
        fetchMyTracks?.cancel()
        fetchMyTracks = viewModelScope.launch {
            try {
                playlistUiState = playlistUiState.copy(isLoading = true)
                val response = fMusicRepository.getAllTrackByPlaylistId(id)
                if (response.isSuccessful) {
                    response.body()?.let { playlist ->
                        playlistUiState = playlistUiState.copy(
                            tracks = Tracks(data = playlist.data.map { itemPlaylist ->
                                itemPlaylist.toTrack()
                            }),
                            isLoading = false,
                            playlist = Playlist(
                                title = playlist.playlistName ?: "",
                                description = "${playlist.data.size} Songs"
                            )
                        )
                    }
                } else {
                    playlistUiState = playlistUiState.copy(isLoading = false)
                }
            } catch (e: Exception) {
                playlistUiState = playlistUiState.copy(isLoading = false)
            } finally {
                fetchMyTracks = null
            }
        }
    }
}
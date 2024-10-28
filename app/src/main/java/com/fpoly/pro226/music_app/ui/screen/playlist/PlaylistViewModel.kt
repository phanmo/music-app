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
    private val idPlaylist: String
) : ViewModel() {
    companion object {

        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<DeezerRepository> {}

        fun provideFactory(
            idPlaylist: String
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = this[MY_REPOSITORY_KEY] as DeezerRepository
                PlaylistViewModel(
                    deezerRepository = myRepository,
                    idPlaylist = idPlaylist
                )
            }
        }
    }

    var playlistUiState by mutableStateOf(PlaylistUiState())
        private set

    private var fetchTracks: Job? = null

    init {
        getPlaylistById(idPlaylist)
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
                }
            } catch (e: Exception) {
                playlistUiState = playlistUiState.copy(isLoading = false)
            } finally {
                fetchTracks = null
            }
        }
    }
}
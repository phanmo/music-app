package com.fpoly.pro226.music_app.ui.screen.main.home

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
import com.fpoly.pro226.music_app.data.source.network.models.Artists
import com.fpoly.pro226.music_app.data.source.network.models.Playlists
import com.fpoly.pro226.music_app.data.source.network.models.Tracks
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


data class HomeUiState(
    val isLoading: Boolean = false,
    val artists: Artists? = null,
    val tracks: Tracks? = null,
    val playlists: Playlists? = null,
)

class HomeViewModel(private val deezerRepository: DeezerRepository) : ViewModel() {
    companion object {

        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<DeezerRepository> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Get the dependency in your factory
                val myRepository = this[MY_REPOSITORY_KEY] as DeezerRepository
                HomeViewModel(
                    deezerRepository = myRepository,
                )
            }
        }
    }

    var homeUiState by mutableStateOf(HomeUiState())
        private set

    private var fetchArtists: Job? = null
    private var fetchTracks: Job? = null
    private var fetchPlaylists: Job? = null

    init {
        getArtistsChart()
        getTrackCharts()
        getPlaylistsChart()
    }


    private fun getTrackCharts() {
        fetchTracks?.cancel()
        fetchTracks = viewModelScope.launch {
            try {
                homeUiState = homeUiState.copy(isLoading = true)
                val response = deezerRepository.getTracksChart()
                if (response.isSuccessful) {
                    response.body()?.let { tracks ->
                        homeUiState = homeUiState.copy(tracks = tracks, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                homeUiState = homeUiState.copy(isLoading = false)
            } finally {
                fetchTracks = null
            }
        }
    }

    private fun getArtistsChart() {
        fetchArtists?.cancel()
        fetchArtists = viewModelScope.launch {
            try {
                homeUiState = homeUiState.copy(isLoading = true)
                val response = deezerRepository.getArtistsChart()
                if (response.isSuccessful) {
                    response.body()?.let { artists ->
                        homeUiState = homeUiState.copy(artists = artists, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                homeUiState = homeUiState.copy(isLoading = false)
            } finally {
                fetchArtists = null
            }
        }
    }

    private fun getPlaylistsChart() {
        fetchPlaylists?.cancel()
        fetchPlaylists = viewModelScope.launch {
            try {
                homeUiState = homeUiState.copy(isLoading = true)
                val response = deezerRepository.getPlaylistsChart()
                if (response.isSuccessful) {
                    response.body()?.let { playlists ->
                        homeUiState = homeUiState.copy(playlists = playlists, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                homeUiState = homeUiState.copy(isLoading = false)
            } finally {
                fetchPlaylists = null
            }
        }
    }

}
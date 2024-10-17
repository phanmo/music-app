package com.fpoly.pro226.music_app.ui.screen.track

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.data.source.network.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


data class TrackUiState(
    val isLoading: Boolean = false,
    val tracks: List<Track> = listOf(),
)

class TrackViewModel(
    private val deezerRepository: DeezerRepository,
    artistId: Int
) : ViewModel() {
    private var fetchTracks: Job? = null

    var tracksUiState by mutableStateOf(TrackUiState())
        private set

    init {
        getTracks(artistId)
    }

    private fun getTracks(artistId: Int) {
        fetchTracks?.cancel()
        fetchTracks = viewModelScope.launch {
            try {
                tracksUiState = tracksUiState.copy(isLoading = true)
                val response = deezerRepository.getTracks(artistId)
                if (response?.isSuccessful == true) {
                    response.body()?.let { tracks ->
                        tracksUiState = tracksUiState.copy(tracks = tracks.data, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                tracksUiState = tracksUiState.copy(isLoading = false)
            } finally {
                fetchTracks = null
            }
        }
    }
}
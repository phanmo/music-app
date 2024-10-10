package com.fpoly.pro226.music_app.ui.screen.song

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.data.source.network.models.NetworkTrack
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


data class SongUiState(
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val currentSong: NetworkTrack? = null,
)

class SongViewModel(
    private val deezerRepository: DeezerRepository
) : ViewModel() {
    private var fetchSong: Job? = null
    var songUiState by mutableStateOf(SongUiState())
        private set

    fun getTrack(id: String = "") {
        fetchSong?.cancel()
        fetchSong = viewModelScope.launch {
            try {
                songUiState = songUiState.copy(isLoading = true)
                val response = deezerRepository.getTrack(true)
                if (response?.isSuccessful == true) {
                    response.body()?.let { track ->
                        songUiState = songUiState.copy(currentSong = track, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                songUiState = songUiState.copy(isLoading = false)
            } finally {
                fetchSong = null
            }
        }
    }
}
package com.fpoly.pro226.music_app.ui.screen.song

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.data.source.network.models.Album
import com.fpoly.pro226.music_app.data.source.network.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


data class SongUiState(
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val currentSong: Track? = null,
    val album: Album? = null,
)

class SongViewModel(
    private val deezerRepository: DeezerRepository
) : ViewModel() {
    private var fetchSong: Job? = null
    private var fetchAlbum: Job? = null
    var songUiState by mutableStateOf(SongUiState())
        private set

    val fetchAlbumEvent = MutableStateFlow<Album?>(null)

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

    fun getAlbum() {
        fetchAlbum?.cancel()
        fetchAlbum = viewModelScope.launch {
//            try {
                songUiState = songUiState.copy(isLoading = true)
                val response = deezerRepository.getAlbum()
                if (response?.isSuccessful == true) {
                    response.body()?.let { album ->
                        fetchAlbumEvent.value = album
//                        songUiState = songUiState.copy(album = album, isLoading = false)
                    }
                }
//            } catch (e: Exception) {
//                songUiState = songUiState.copy(isLoading = false)
//            } finally {
//                fetchAlbum = null
//            }
        }
    }
}
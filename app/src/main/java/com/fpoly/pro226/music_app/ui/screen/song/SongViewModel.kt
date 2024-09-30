package com.fpoly.pro226.music_app.ui.screen.song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SongViewModel(
    private val deezerRepository: DeezerRepository
) : ViewModel() {
    private val _songUiState = MutableStateFlow(SongUiState())
    val songUiState: StateFlow<SongUiState> = _songUiState.asStateFlow()

    fun getTrack(id: String) {
        viewModelScope.launch {
            val response = deezerRepository.getTrack(true)
            if (response?.isSuccessful == true) {
                response.body()?.let { track ->
                    _songUiState.update {
                        it.copy(currentSong = track)
                    }
                }

            }
        }
    }
}
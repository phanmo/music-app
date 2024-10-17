package com.fpoly.pro226.music_app.ui.screen.genre

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.data.source.network.models.Artist
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


data class GenreUiState(
    val isLoading: Boolean = false,
    val artists: List<Artist> = listOf(),
)

class GenreViewModel(
    private val deezerRepository: DeezerRepository,
    genreId: Int
) : ViewModel() {
    private var fetchArtists: Job? = null
    var genreUiState by mutableStateOf(GenreUiState())
        private set

    init {
        getArtists(genreId)
    }


    private fun getArtists(genreId: Int) {
        fetchArtists?.cancel()
        fetchArtists = viewModelScope.launch {
            try {
                genreUiState = genreUiState.copy(isLoading = true)
                val response = deezerRepository.getArtists(genreId)
                if (response?.isSuccessful == true) {
                    response.body()?.let { artists ->
                        genreUiState = genreUiState.copy(artists = artists.data, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                genreUiState = genreUiState.copy(isLoading = false)
            } finally {
                fetchArtists = null
            }
        }
    }
}
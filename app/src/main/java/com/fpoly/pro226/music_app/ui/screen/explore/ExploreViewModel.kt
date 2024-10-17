package com.fpoly.pro226.music_app.ui.screen.explore

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.data.source.network.models.Genres
import com.fpoly.pro226.music_app.data.source.network.models.Radios
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


data class ExploreUiState(
    val isLoading: Boolean = false,
    val genres: Genres? = null,
    val radios: Radios? = null,
)

class ExploreViewModel(private val deezerRepository: DeezerRepository) : ViewModel() {
    private var fetchGenres: Job? = null
    private var fetchRadios: Job? = null
    var exploreUiState by mutableStateOf(ExploreUiState())
        private set

    init {
        getGenres()
        getRadios()
        Log.d("TAG", "ExploreViewModel init")
    }

    private fun getGenres() {
        fetchGenres?.cancel()
        fetchGenres = viewModelScope.launch {
            try {
                exploreUiState = exploreUiState.copy(isLoading = true)
                val response = deezerRepository.getGenres()
                if (response?.isSuccessful == true) {
                    response.body()?.let { genres ->
                        exploreUiState = exploreUiState.copy(genres = genres, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                exploreUiState = exploreUiState.copy(isLoading = false)
            } finally {
                fetchGenres = null
            }
        }
    }

    private fun getRadios() {
        fetchRadios?.cancel()
        fetchRadios = viewModelScope.launch {
            try {
                exploreUiState = exploreUiState.copy(isLoading = true)
                val response = deezerRepository.getRadios()
                if (response?.isSuccessful == true) {
                    response.body()?.let { radios ->
                        exploreUiState = exploreUiState.copy(radios = radios, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                exploreUiState = exploreUiState.copy(isLoading = false)
            } finally {
                fetchRadios = null
            }
        }
    }

}
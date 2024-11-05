package com.fpoly.pro226.music_app.ui.screen.main.explore

import android.util.Log
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
import com.fpoly.pro226.music_app.data.source.network.models.Genres
import com.fpoly.pro226.music_app.data.source.network.models.Radios
import com.fpoly.pro226.music_app.data.source.network.models.Tracks
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


data class ExploreUiState(
    val isLoading: Boolean = false,
    val genres: Genres? = null,
    val radios: Radios? = null,
    val resulTrack: Tracks? = null,
)

class ExploreViewModel(private val deezerRepository: DeezerRepository) : ViewModel() {

    companion object {

        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<DeezerRepository> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Get the dependency in your factory
                val myRepository = this[MY_REPOSITORY_KEY] as DeezerRepository
                ExploreViewModel(
                    deezerRepository = myRepository,
                )
            }
        }
    }

    private var fetchGenres: Job? = null
    private var fetchRadios: Job? = null
    private var fetchTrack: Job? = null
    var exploreUiState by mutableStateOf(ExploreUiState())
        private set


    init {
        getGenres()
        getRadios()
        Log.d("TAG", "ExploreViewModel init")
    }

    override fun onCleared() {
        Log.d("TAG", "ExploreViewModel onCleared")

        super.onCleared()
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

    fun searchTrack(query: String) {
        fetchTrack?.cancel()
        fetchTrack = viewModelScope.launch {
            try {
                exploreUiState = exploreUiState.copy(isLoading = true)
                val response = deezerRepository.searchTrack(query)
                if (response.isSuccessful) {
                    response.body()?.let { tracks ->
                        Log.d("HAHA", "searchTrack: $query")
                        exploreUiState = exploreUiState.copy(resulTrack = tracks, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                exploreUiState = exploreUiState.copy(isLoading = false)
            } finally {
                fetchTrack = null
            }
        }
    }

    fun clearTracks() {
        exploreUiState = exploreUiState.copy(resulTrack = null)
    }

}
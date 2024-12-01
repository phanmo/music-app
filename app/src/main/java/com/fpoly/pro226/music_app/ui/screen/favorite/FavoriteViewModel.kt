package com.fpoly.pro226.music_app.ui.screen.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fpoly.pro226.music_app.data.models.TrackDestination
import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.data.repositories.FMusicRepository
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.ui.screen.game.GameViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


data class FavoriteUiState(
    val isLoading: Boolean = false,
    val tracks: List<Track> = listOf(),
)

class FavoriteViewModel(
    private val fMusicRepository: FMusicRepository,
) : ViewModel() {

    companion object {
        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}

        fun provideFactory(userId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fMusicRepo = this[MY_REPOSITORY_KEY] as FMusicRepository
                FavoriteViewModel(
                    fMusicRepository = fMusicRepo,
                )
            }
        }
    }

    private var fetchTracks: Job? = null

    var tracksUiState by mutableStateOf(FavoriteUiState())
        private set

    init {

    }

    private fun getTracks(artistId: String) {
//        fetchTracks?.cancel()
//        fetchTracks = viewModelScope.launch {
//            try {
//                tracksUiState = tracksUiState.copy(isLoading = true)
//                val response = deezerRepository.getTracks(artistId)
//                if (response?.isSuccessful == true) {
//                    response.body()?.let { tracks ->
//                        tracksUiState = tracksUiState.copy(tracks = tracks.data, isLoading = false)
//                    }
//                }
//            } catch (e: Exception) {
//                tracksUiState = tracksUiState.copy(isLoading = false)
//            } finally {
//                fetchTracks = null
//            }
//        }
    }

    private fun getTracksByRadioId(radioId: String) {
        fetchTracks?.cancel()
//        fetchTracks = viewModelScope.launch {
//            try {
//                tracksUiState = tracksUiState.copy(isLoading = true)
//                val response = deezerRepository.getTracksByRadioId(radioId)
//                if (response?.isSuccessful == true) {
//                    response.body()?.let { tracks ->
//                        tracksUiState = tracksUiState.copy(tracks = tracks.data, isLoading = false)
//                    }
//                }
//            } catch (e: Exception) {
//                tracksUiState = tracksUiState.copy(isLoading = false)
//            } finally {
//                fetchTracks = null
//            }
//        }
    }
}
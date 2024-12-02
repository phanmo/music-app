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
import com.fpoly.pro226.music_app.data.repositories.FMusicRepository
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.favorite.toTrack
import com.fpoly.pro226.music_app.data.source.network.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


data class FavoriteUiState(
    val isLoading: Boolean = false,
    val tracks: List<Track> = listOf(),
)

class FavoriteViewModel(
    private val fMusicRepository: FMusicRepository,
    private val userId: String = ""
) : ViewModel() {

    companion object {
        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}

        fun provideFactory(userId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fMusicRepo = this[MY_REPOSITORY_KEY] as FMusicRepository
                FavoriteViewModel(
                    fMusicRepository = fMusicRepo,
                    userId = userId ?: ""
                )
            }
        }
    }

    private var fetchFav: Job? = null

    var favoriteUiState by mutableStateOf(FavoriteUiState())
        private set

    init {
        getFavorites()
    }

    private fun getFavorites() {
        fetchFav?.cancel()
        fetchFav = viewModelScope.launch {
            try {
                favoriteUiState = favoriteUiState.copy(isLoading = true)
                val response = fMusicRepository.getFavorite(userId)
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        favoriteUiState = favoriteUiState.copy(
                            tracks = res.data.map { it.toTrack() },
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                favoriteUiState = favoriteUiState.copy(isLoading = false)
            } finally {
                fetchFav = null
            }
        }
    }

    fun reloadDataLocal() {
        favoriteUiState =
            favoriteUiState.copy(tracks = fMusicRepository.currentFavorites.map { it.toTrack() })
    }
}
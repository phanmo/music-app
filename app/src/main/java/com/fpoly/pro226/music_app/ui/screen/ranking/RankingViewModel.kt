package com.fpoly.pro226.music_app.ui.screen.ranking

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
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.ranking.RankingResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

data class RankingUiState(
    val isLoading: Boolean = false,
    val rankingResponse: RankingResponse? = null
)

class RankingViewModel(
    private val fMusicRepository: FMusicRepository
) : ViewModel() {

    companion object {

        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}

        fun provideFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fMusicRepo = this[MY_REPOSITORY_KEY] as FMusicRepository
                RankingViewModel(
                    fMusicRepository = fMusicRepo,
                )
            }
        }
    }

    private var fetchRanking: Job? = null

    var rankingUiState by mutableStateOf(RankingUiState())
        private set

    fun getRanking() {
        fetchRanking?.cancel()
        fetchRanking = viewModelScope.launch {
            try {
                rankingUiState = rankingUiState.copy(isLoading = true)
                val response = fMusicRepository.getRanking()
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        rankingUiState = rankingUiState.copy(
                            rankingResponse = res,
                            isLoading = false,
                        )
                    }
                } else {
                    rankingUiState = rankingUiState.copy(isLoading = false)
                }
            } catch (e: Exception) {
                rankingUiState = rankingUiState.copy(isLoading = false)
            } finally {
                fetchRanking = null
            }
        }
    }
}
package com.fpoly.pro226.music_app.ui.screen.game

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
import com.fpoly.pro226.music_app.data.repositories.FMusicRepository
import com.fpoly.pro226.music_app.data.source.network.models.Radios
import com.fpoly.pro226.music_app.data.source.network.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


data class GameUiState(
    val isLoading: Boolean = false,
    val radios: Radios? = null,
    val tracksQuestion: List<Track> = listOf(),
    val trackAnswer: Track? = null
)

class GameViewModel(
    private val fMusicRepository: FMusicRepository,
    private val deezerRepository: DeezerRepository
) : ViewModel() {
    companion object {

        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}
        val MY_REPOSITORY_KEY_2 = object : CreationExtras.Key<DeezerRepository> {}

        fun provideFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fMusicRepo = this[MY_REPOSITORY_KEY] as FMusicRepository
                val deezerRepo = this[MY_REPOSITORY_KEY_2] as DeezerRepository

                GameViewModel(
                    fMusicRepository = fMusicRepo,
                    deezerRepository = deezerRepo
                )
            }
        }
    }

    private var fetchRadios: Job? = null
    private var fetchTracks: Job? = null

    var gameUiState by mutableStateOf(GameUiState())
        private set

    init {
        getRadios()
    }

    private fun getRadios() {
        fetchRadios?.cancel()
        fetchRadios = viewModelScope.launch {
            try {
                gameUiState = gameUiState.copy(isLoading = true)
                val response = deezerRepository.getRadios()
                if (response?.isSuccessful == true) {
                    response.body()?.let { radios ->
                        gameUiState = gameUiState.copy(radios = radios, isLoading = false)
                        nextQuestion()
                    }
                }
            } catch (e: Exception) {
                gameUiState = gameUiState.copy(isLoading = false)
            } finally {
                fetchRadios = null
            }
        }
    }

    private fun getTracksByRadioId(radioId: String) {
        fetchTracks?.cancel()
        fetchTracks = viewModelScope.launch {
            try {
                gameUiState = gameUiState.copy(isLoading = true)
                val response = deezerRepository.getTracksByRadioId(radioId)
                if (response?.isSuccessful == true) {
                    response.body()?.let { tracks ->
                        if (tracks.data.size < 4) {
                            gameUiState.radios?.let {
                                val idRadio = it.data.random().id
                                getTracksByRadioId(idRadio)
                            }
                        } else {
                            val randomPickQuestion = tracks.data.shuffled().take(4)
                            val randomPickAnswer = randomPickQuestion.random()
                            gameUiState = gameUiState.copy(
                                tracksQuestion = randomPickQuestion,
                                isLoading = false,
                                trackAnswer = randomPickAnswer
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                gameUiState = gameUiState.copy(isLoading = false)
            } finally {
                fetchTracks = null
            }

        }
    }

    fun nextQuestion() {
        gameUiState.radios?.let {
            val idRadio = it.data.random().id
            getTracksByRadioId(idRadio)
        }
    }

    fun selectAnswer(idTrack: String): Boolean {
        return idTrack == gameUiState.trackAnswer?.id
    }
}
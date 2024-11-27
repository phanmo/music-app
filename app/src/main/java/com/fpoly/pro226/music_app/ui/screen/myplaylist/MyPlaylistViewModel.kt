package com.fpoly.pro226.music_app.ui.screen.myplaylist

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
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlayListResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlaylistBody
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


data class MyPlaylistUiState(
    val isLoading: Boolean = false,
    val playListResponse: PlayListResponse? = null
)

class MyPlaylistViewModel(
    private val fMusicRepository: FMusicRepository,
    private val userId: String?,
) : ViewModel() {
    companion object {

        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}

        fun provideFactory(
            userId: String?
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = this[MY_REPOSITORY_KEY] as FMusicRepository
                MyPlaylistViewModel(
                    fMusicRepository = myRepository,
                    userId = userId
                )
            }
        }
    }

    private var fetchPlaylists: Job? = null
    private var addPlaylistJob: Job? = null
    private var deletePlaylistJob: Job? = null

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    var myPlaylistUiState by mutableStateOf(MyPlaylistUiState())
        private set

    init {
        getAllPlaylist()
    }


    suspend fun showToast(message: String) {
        _toastEvent.emit(message)
    }

    fun getAllPlaylist() {
        userId?.let { id ->
            fetchPlaylists?.cancel()
            fetchPlaylists = viewModelScope.launch {
                try {
                    myPlaylistUiState = myPlaylistUiState.copy(isLoading = true)
                    val response = fMusicRepository.getPlaylist(id)
                    if (response.isSuccessful) {
                        response.body()?.let { res ->
                            myPlaylistUiState = myPlaylistUiState.copy(
                                playListResponse = res,
                                isLoading = false,
                            )
                        }
                    } else {
                        myPlaylistUiState = myPlaylistUiState.copy(isLoading = false)
                    }
                } catch (e: Exception) {
                    myPlaylistUiState = myPlaylistUiState.copy(isLoading = false)
                } finally {
                    fetchPlaylists = null
                }
            }
        }

    }

    fun deletePlaylist(idPlaylist: String) {
        deletePlaylistJob?.cancel()
        deletePlaylistJob = viewModelScope.launch {
            try {
                myPlaylistUiState = myPlaylistUiState.copy(isLoading = true)
                val response = fMusicRepository.deletePlaylist(idPlaylist)
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        myPlaylistUiState = myPlaylistUiState.copy(
                            playListResponse = res,
                            isLoading = false,
                        )
                    }
                } else {
                    myPlaylistUiState = myPlaylistUiState.copy(isLoading = false)
                }
            } catch (e: Exception) {
                myPlaylistUiState = myPlaylistUiState.copy(isLoading = false)
            } finally {
                deletePlaylistJob = null
            }
        }


    }

    fun addPlaylist(name: String) {
        userId?.let { id ->
            addPlaylistJob?.cancel()
            addPlaylistJob = viewModelScope.launch {
                try {
                    val playlistBody = PlaylistBody(id_user = id, name = name)
                    myPlaylistUiState = myPlaylistUiState.copy(isLoading = true)
                    val response = fMusicRepository.addPlaylist(playlistBody)
                    if (response.isSuccessful) {
                        getAllPlaylist()
                    } else if (response.code() == 400) {
                        response.errorBody()?.let { res ->
                            try {
                                val errorResponse =
                                    Gson().fromJson(res.string(), PlayListResponse::class.java)
                                showToast(errorResponse.message)
                                myPlaylistUiState = myPlaylistUiState.copy(isLoading = false)

                            } catch (e: Exception) {
                                myPlaylistUiState = myPlaylistUiState.copy(isLoading = false)
                            }

                        }
                    }
                } catch (e: Exception) {
                    myPlaylistUiState = myPlaylistUiState.copy(isLoading = false)
                } finally {
                    addPlaylistJob = null
                }
            }
        }

    }

}
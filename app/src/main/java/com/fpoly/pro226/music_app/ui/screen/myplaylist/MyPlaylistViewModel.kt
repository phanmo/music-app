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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


data class MyPlaylistUiState(
    val isLoading: Boolean = false,
    val playListResponse: PlayListResponse? = null
)

class MyPlaylistViewModel(
    private val fMusicRepository: FMusicRepository,
) : ViewModel() {
    companion object {

        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}

        fun provideFactory(
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = this[MY_REPOSITORY_KEY] as FMusicRepository
                MyPlaylistViewModel(
                    fMusicRepository = myRepository,
                )
            }
        }
    }

    var myPlaylistUiState by mutableStateOf(MyPlaylistUiState())
        private set

    private var fetchPlaylists: Job? = null
    private var addPlaylistJob: Job? = null

    private var _userId: String = ""

    fun getAllPlaylist(idUser: String) {
        setCurrentUserId(idUser)
        fetchPlaylists?.cancel()
        fetchPlaylists = viewModelScope.launch {
            try {
                myPlaylistUiState = myPlaylistUiState.copy(isLoading = true)
                val response = fMusicRepository.getPlaylist(idUser)
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        myPlaylistUiState = myPlaylistUiState.copy(
                            playListResponse = res,
                            isLoading = false,
                        )
                    }
                }
            } catch (e: Exception) {
                myPlaylistUiState = myPlaylistUiState.copy(isLoading = false)
            } finally {
                fetchPlaylists = null
            }
        }
    }

    fun setCurrentUserId(id: String) {
        _userId = id
    }

    fun addPlaylist(name: String) {
        addPlaylistJob?.cancel()
        addPlaylistJob = viewModelScope.launch {
            try {
                val playlistBody = PlaylistBody(id_user = _userId, name = name)
                myPlaylistUiState = myPlaylistUiState.copy(isLoading = true)
                val response = fMusicRepository.addPlaylist(playlistBody)
                if (response.isSuccessful) {
                    getAllPlaylist(_userId)
                }
            } catch (e: Exception) {
                myPlaylistUiState = myPlaylistUiState.copy(isLoading = false)
            } finally {
                addPlaylistJob = null
            }
        }
    }

}
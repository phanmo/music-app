package com.fpoly.pro226.music_app.ui.screen.downloaded

import android.os.Environment
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
import com.fpoly.pro226.music_app.data.repositories.FMusicRepository
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.favorite.FavoriteBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.favorite.toTrack
import com.fpoly.pro226.music_app.data.source.network.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

data class DownloadedUiState(
    val isLoading: Boolean = false,
    val tracks: List<Track> = listOf(),
)

class DownloadedViewModel(
    private val fMusicRepository: FMusicRepository,
    private val userId: String = ""
) : ViewModel() {
    companion object {
        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}

        fun provideFactory(userId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fMusicRepo = this[MY_REPOSITORY_KEY] as FMusicRepository
                DownloadedViewModel(
                    fMusicRepository = fMusicRepo,
                    userId = userId ?: ""
                )
            }
        }
    }

    private var fetchFav: Job? = null

    var downloadedUiState by mutableStateOf(DownloadedUiState())
        private set

    init {
        getListDownloaded()
    }

    private fun getAllSongsLocal(): List<File> {
        val musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        return musicDir.listFiles()?.filter {
            it.isFile && (it.extension == "mp3")
        } ?: emptyList()
    }

    private fun synchronizeTracks(tracksFromDB: List<Track>): List<Track> {
        val track = mutableListOf<Track>()
        val songs = getAllSongsLocal()
        tracksFromDB.forEach { trackFromDB ->
            songs.forEach { song ->
                //song.name = "${currentTrack.id}]~[${currentTrack.title}]~[${currentTrack.artist}"
                if (song.name.contains("]~[")) {
                    val properties = song.name.split("]~[")
                    val songInfo = mapOf(
                        "id" to properties[0],
                        "title" to properties[1],
                        "artist" to properties[2]
                    )
                    if (songInfo["id"] == trackFromDB.id) {
                        track.add(trackFromDB.copy(preview = song.absolutePath))
                    }

                }

            }
        }
        return track
    }

    private fun getListDownloaded() {
        fetchFav?.cancel()
        fetchFav = viewModelScope.launch {
            try {
                downloadedUiState = downloadedUiState.copy(isLoading = true)
                val response = fMusicRepository.getListDownloaded(userId)
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        val convertTrack = res.data.map { it.toTrack() }
                        val tracks = synchronizeTracks(convertTrack)
                        downloadedUiState = downloadedUiState.copy(
                            tracks = tracks,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                downloadedUiState = downloadedUiState.copy(isLoading = false)
            } finally {
                fetchFav = null
            }
        }
    }

    fun addFavorite(favoriteBody: FavoriteBody) {
        viewModelScope.launch {
            try {
                downloadedUiState = downloadedUiState.copy(isLoading = true)
                val response = fMusicRepository.addFavorite(favoriteBody.copy(id_user = userId))
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        downloadedUiState = downloadedUiState.copy(
                            isLoading = false,
                        )
                    }
                } else {
                    downloadedUiState = downloadedUiState.copy(isLoading = false)
                }
            } catch (e: Exception) {
                downloadedUiState = downloadedUiState.copy(isLoading = false)
            }
        }
    }

}
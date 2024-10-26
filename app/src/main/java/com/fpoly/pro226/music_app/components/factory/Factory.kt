package com.fpoly.pro226.music_app.components.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fpoly.pro226.music_app.data.models.TrackDestination
import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.ui.screen.explore.ExploreViewModel
import com.fpoly.pro226.music_app.ui.screen.genre.GenreViewModel
import com.fpoly.pro226.music_app.ui.screen.song.SongViewModel
import com.fpoly.pro226.music_app.ui.screen.track.TrackViewModel

interface Factory<T> {
    fun create(id: String = "0"): T {
        throw (Throwable("Not yet implemented"))
    }

    fun createTrackDestination(trackDestination: TrackDestination): T {
        throw (Throwable("Not yet implemented"))
    }
}

class SongViewModelFactory(private val deezerRepository: DeezerRepository) :
    Factory<SongViewModel> {
    override fun create(id: String): SongViewModel = SongViewModel(deezerRepository)

}

class GenreViewModelFactory(private val deezerRepository: DeezerRepository) :
    Factory<GenreViewModel> {
    override fun create(id: String): GenreViewModel = GenreViewModel(deezerRepository, id)

}

class TrackViewModelFactory(
    private val trackDestination: TrackDestination,
    private val deezerRepository: DeezerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrackViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrackViewModel(deezerRepository, trackDestination) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


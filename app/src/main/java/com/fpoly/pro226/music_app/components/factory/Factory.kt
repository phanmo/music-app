package com.fpoly.pro226.music_app.components.factory

import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.ui.screen.explore.ExploreViewModel
import com.fpoly.pro226.music_app.ui.screen.genre.GenreViewModel
import com.fpoly.pro226.music_app.ui.screen.song.SongViewModel
import com.fpoly.pro226.music_app.ui.screen.track.TrackViewModel

interface Factory<T> {
    fun create(int: Int = 0): T
}

class SongViewModelFactory(private val deezerRepository: DeezerRepository) :
    Factory<SongViewModel> {
    override fun create(int: Int): SongViewModel = SongViewModel(deezerRepository)

}

class ExploreViewModelFactory(private val deezerRepository: DeezerRepository) :
    Factory<ExploreViewModel> {
    override fun create(int: Int): ExploreViewModel = ExploreViewModel(deezerRepository)

}

class GenreViewModelFactory(private val deezerRepository: DeezerRepository) :
    Factory<GenreViewModel> {
    override fun create(int: Int): GenreViewModel = GenreViewModel(deezerRepository, int)

}

class TrackViewModelFactory(private val deezerRepository: DeezerRepository) :
    Factory<TrackViewModel> {
    override fun create(int: Int): TrackViewModel = TrackViewModel(deezerRepository, int)

}
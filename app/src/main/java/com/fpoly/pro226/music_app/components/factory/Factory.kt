package com.fpoly.pro226.music_app.components.factory

import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.ui.screen.song.SongViewModel

interface Factory<T> {
    fun create(): T
}

class SongViewModelFactory(private val deezerRepository: DeezerRepository) :
    Factory<SongViewModel> {
    override fun create(): SongViewModel = SongViewModel(deezerRepository)

}
package com.fpoly.pro226.music_app.ui.screen.song

import com.fpoly.pro226.music_app.data.source.network.models.TrackApiModel

data class SongUiState(
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val currentSong: TrackApiModel? = null,
)
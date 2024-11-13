package com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist

data class PlayListResponse(
    val `data`: List<Playlists>,
    val message: String,
    val status: Int
)
package com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist

data class AddItemPlaylistResponse(
    val `data`: List<ItemPlaylist>,
    val message: String,
    val status: Int
)
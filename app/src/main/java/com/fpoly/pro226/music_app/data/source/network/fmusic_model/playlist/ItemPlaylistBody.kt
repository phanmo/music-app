package com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist

data class ItemPlaylistBody(
    val id_playlist: String,
    val id_track: String,
    val name: String,
    val image_url: String,
    val preViewUrl: String,
)
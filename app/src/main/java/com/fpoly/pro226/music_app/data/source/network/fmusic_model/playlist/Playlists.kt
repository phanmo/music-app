package com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist

data class Playlists(
    val _id: String,
    val createdAt: String,
    val id_user: String,
    val name: String,
    val playlistItems: List<PlaylistItem>,
    val updatedAt: String
)
package com.fpoly.pro226.music_app.data.source.network.fmusic_model.favorite

data class FavoriteBody(
    val __v: Int,
    val _id: String,
    val album: String,
    val artist: String,
    val createdAt: String,
    val id_track: String,
    val id_user: String,
    val image_url: String,
    val name: String,
    val preViewUrl: String,
    val updatedAt: String
)
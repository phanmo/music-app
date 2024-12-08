package com.fpoly.pro226.music_app.data.source.network.models

data class Radio(
    val id: String,
    val md5_image: String,
    val picture: String,
    val picture_big: String,
    val picture_medium: String,
    val picture_small: String,
    val picture_xl: String,
    val title: String,
    val tracklist: String,
    val type: String
)
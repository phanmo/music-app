package com.fpoly.pro226.music_app.data.source.network.models

data class Artist(
    val id: String,
    val link: String,
    val name: String,
    val picture: String,
    val picture_big: String,
    val picture_medium: String,
    val picture_small: String,
    val picture_xl: String,
    val radio: Boolean,
    val share: String,
    val tracklist: String,
    val type: String
)
package com.fpoly.pro226.music_app.data.source.network.models

data class Album(
    val cover: String,
    val cover_big: String,
    val cover_medium: String,
    val cover_small: String,
    val cover_xl: String,
    val id: String,
    val link: String,
    val md5_image: String,
    val release_date: String,
    val title: String,
    val tracklist: String,
    val type: String
)
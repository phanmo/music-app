package com.fpoly.pro226.music_app.data.source.network.models

data class Playlist(
    val checksum: String = "",
    val creation_date: String = "",
    val id: Long = 0L,
    val link: String = "",
    val md5_image: String = "",
    val nb_tracks: Int = 0,
    val picture: String = "",
    val description: String = "",
    val picture_big: String = "",
    val picture_medium: String = "",
    val picture_small: String = "",
    val picture_type: String = "",
    val picture_xl: String = "",
    val `public`: Boolean = true,
    val title: String = "",
    val tracks: Tracks? = null,
    val tracklist: String = "",
    val type: String = "",
)
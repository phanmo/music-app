package com.fpoly.pro226.music_app.data.source.network.fmusic_model.favorite

import com.fpoly.pro226.music_app.data.source.network.models.Album
import com.fpoly.pro226.music_app.data.source.network.models.Artist
import com.fpoly.pro226.music_app.data.source.network.models.Track

data class FavoriteBody(
    val __v: Int = 0,
    val _id: String = "",
    val album: String?,
    val artist: String?,
    val createdAt: String = "",
    val id_track: String,
    val id_user: String = "",
    val image_url: String,
    val name: String,
    val preViewUrl: String,
    val updatedAt: String = ""
)

fun FavoriteBody.toTrack(): Track {
    return Track(
        id = id_track,
        preview = preViewUrl,
        title = name,
        album = Album(
            cover_medium = image_url,
            title = album ?: "Your Liked Songs",
            tracklist = "REQUIRED NOT EMPTY"
        ),
        artist = Artist(name = artist ?: "### ARTIST Update late")
    )
}
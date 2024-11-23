package com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist

import com.fpoly.pro226.music_app.data.source.network.models.Album
import com.fpoly.pro226.music_app.data.source.network.models.Artist
import com.fpoly.pro226.music_app.data.source.network.models.Track

data class ItemPlaylist(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val id_playlist: String,
    val id_track: String,
    val image_url: String,
    val name: String,
    val preViewUrl: String,
    val updatedAt: String,
    val artistName: String? = null,
    val albumName: String? = null
)

fun ItemPlaylist.toTrack(): Track {
    return Track(
        id = id_track,
        preview = preViewUrl,
        title = name,
        album = Album(
            cover_medium = image_url,
            title = albumName ?: "### ALBUM Update late",
            tracklist = "REQUIRED NOT EMPTY"
        ),
        artist = Artist(name = artistName ?: "### ARTIST Update late")
    )
}
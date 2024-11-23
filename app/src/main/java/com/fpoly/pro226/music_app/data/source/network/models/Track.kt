package com.fpoly.pro226.music_app.data.source.network.models

import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.ItemPlaylistBody

data class Track(
    val album: Album,
    val artist: Artist,
    val available_countries: List<String>,
    val bpm: Double,
    val contributors: List<Contributor>,
    val disk_number: Int,
    val duration: String,
    val explicit_content_cover: Int,
    val explicit_content_lyrics: Int,
    val explicit_lyrics: Boolean,
    val gain: Double,
    val id: String,
    val isrc: String,
    val link: String,
    val md5_image: String,
    val preview: String,
    val rank: String,
    val readable: Boolean,
    val release_date: String,
    val share: String,
    val title: String,
    val title_short: String,
    val title_version: String,
    val track_position: Int,
    val track_token: String,
    val type: String
)

fun Track.toItemPlaylistBody(idPlaylist: String): ItemPlaylistBody {
    return ItemPlaylistBody(
        id_playlist = idPlaylist,
        id_track = this.id,
        image_url = this.album.cover_medium,
        name = this.title,
        preViewUrl = this.preview
    )
}
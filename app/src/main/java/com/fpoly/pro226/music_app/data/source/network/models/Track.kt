package com.fpoly.pro226.music_app.data.source.network.models

import com.fpoly.pro226.music_app.data.source.network.fmusic_model.favorite.FavoriteBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.ItemPlaylistBody

data class Track(
    val album: Album? = null,
    val artist: Artist? = null,
    val available_countries: List<String> = emptyList(),
    val bpm: Double = 2.0,
    val contributors: List<Contributor> = emptyList(),
    val disk_number: Int = 0,
    val duration: String = "",
    val explicit_content_cover: Int = 0,
    val explicit_content_lyrics: Int = 0,
    val explicit_lyrics: Boolean = false,
    val gain: Double = 2.0,
    val id: String,
    val isrc: String = "",
    val link: String = "",
    val md5_image: String = "",
    val preview: String,
    val rank: String = "",
    val readable: Boolean = false,
    val release_date: String = "",
    val share: String = "",
    val title: String = "",
    val title_short: String = "",
    val title_version: String = "",
    val track_position: Int = 0,
    val track_token: String = "",
    val type: String = ""
)

fun Track.toItemPlaylistBody(idPlaylist: String): ItemPlaylistBody {
    return ItemPlaylistBody(
        id_playlist = idPlaylist,
        id_track = this.id,
        image_url = this.album?.cover_medium ?: "",
        name = this.title,
        preViewUrl = this.preview,
        artist = this.artist?.name ?: "",
        album = this.album?.title ?: ""
    )
}

fun Track.toFavoriteBody(): FavoriteBody {
    return FavoriteBody(
        id_track = this.id,
        name = this.title,
        image_url = this.album?.cover_medium ?: "",
        preViewUrl = this.preview,
        artist = this.artist?.name ?: "",
        album = this.album?.title ?: ""
    )

}
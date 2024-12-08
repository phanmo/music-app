package com.fpoly.pro226.music_app.data.models

sealed class TrackDestination {
    data class Album(val id: String) : TrackDestination()
    data class Genre(val id: String) : TrackDestination()
    data class Artist(val id: String) : TrackDestination()
    data class Radio(val id: String) : TrackDestination()
}
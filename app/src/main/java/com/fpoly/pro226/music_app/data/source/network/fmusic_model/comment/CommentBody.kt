package com.fpoly.pro226.music_app.data.source.network.fmusic_model.comment

data class CommentBody(
    val content: String,
    val id_track: String,
    val id_user: String? = null,
)
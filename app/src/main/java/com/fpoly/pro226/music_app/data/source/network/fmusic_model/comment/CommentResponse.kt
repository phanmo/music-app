package com.fpoly.pro226.music_app.data.source.network.fmusic_model.comment

data class CommentResponse(
    val `data`: List<Comment>,
    val message: String,
    val status: Int
)
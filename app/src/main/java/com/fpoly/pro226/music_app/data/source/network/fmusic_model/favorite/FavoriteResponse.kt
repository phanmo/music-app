package com.fpoly.pro226.music_app.data.source.network.fmusic_model.favorite

data class FavoriteResponse(
    val `data`: List<FavoriteBody>,
    val message: String,
    val status: Int
)
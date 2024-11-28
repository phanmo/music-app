package com.fpoly.pro226.music_app.data.source.network.fmusic_model.ranking

data class RankingResponse(
    val `data`: List<RankingUser>,
    val message: String,
    val status: Int
)
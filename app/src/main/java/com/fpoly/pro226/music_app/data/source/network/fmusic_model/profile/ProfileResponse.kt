package com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile

import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.UserInfo

data class ProfileResponse(
    val `data`: UserInfo,
    val messenger: String,
    val status: Int
)
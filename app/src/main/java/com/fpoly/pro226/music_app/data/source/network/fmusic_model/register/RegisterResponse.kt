package com.fpoly.pro226.music_app.data.source.network.fmusic_model.register

import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.UserInfo

class RegisterResponse (
    val status: Int,
    val message: String,
    val data: UserInfo
)
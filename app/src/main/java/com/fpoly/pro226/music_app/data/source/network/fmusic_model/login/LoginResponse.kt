package com.fpoly.pro226.music_app.data.source.network.fmusic_model.login

data class LoginResponse(
    val message: String,
    val refreshToken: String,
    val status: Int,
    val token: String,
    val data: UserResponse
)
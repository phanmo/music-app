package com.fpoly.pro226.music_app.data.source.network.fmusic_model.login

data class UserResponse(
    val __v: Int,
    val _id: String,
    val available: Boolean,
    val createdAt: String,
    val email: String,
    val password: String,
    val point: Int,
    val updatedAt: String
)
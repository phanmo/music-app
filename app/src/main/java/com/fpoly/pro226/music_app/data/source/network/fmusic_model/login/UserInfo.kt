package com.fpoly.pro226.music_app.data.source.network.fmusic_model.login

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

data class UserInfo(
    val __v: Int,
    val _id: String,
    val available: Boolean,
    val createdAt: String,
    val email: String,
    val username: String,
    val coin: Int,
    val birthday: String,
    val name: String,
    val avatar: String,
    val password: String,
    val updatedAt: String
)
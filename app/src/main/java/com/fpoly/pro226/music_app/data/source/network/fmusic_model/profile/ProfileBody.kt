package com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile

import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.UserInfo
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

data class ProfileBody(
    val birthday: String,
    val name: String,
    val email: String,
    val username: String,
)

fun ProfileBody.toJsonPart(): RequestBody {
    val gson = Gson()
    val jsonString = gson.toJson(this)
    return jsonString.toRequestBody("application/json".toMediaTypeOrNull())
}
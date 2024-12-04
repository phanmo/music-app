package com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class ProfileBody(
    var birthday: String,
    var name: String,
    var email: String,
    var username: String,
)

fun ProfileBody.toMultipartParams(): Map<String, RequestBody> {
    return mapOf(
        "name" to name.toRequestBody("text/plain".toMediaTypeOrNull()),
        "birthday" to birthday.toRequestBody("text/plain".toMediaTypeOrNull()),
        "username" to username.toRequestBody("text/plain".toMediaTypeOrNull())
    )
}

fun ProfileBody.toAvatarPart(avatar: File?): MultipartBody.Part? {
    return if (avatar != null) {
        val requestFile = avatar.asRequestBody("image/jpeg".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("avatar", avatar.name, requestFile)
    } else {
        null
    }
}
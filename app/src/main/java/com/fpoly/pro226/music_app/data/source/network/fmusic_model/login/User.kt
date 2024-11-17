package com.fpoly.pro226.music_app.data.source.network.fmusic_model.login

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)
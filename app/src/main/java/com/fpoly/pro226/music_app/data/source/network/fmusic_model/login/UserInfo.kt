package com.fpoly.pro226.music_app.data.source.network.fmusic_model.login

import java.time.ZoneId
import java.time.ZonedDateTime

data class UserInfo(
    val __v: Int,
    val _id: String,
    val available: Boolean,
    val createdAt: String,
    val email: String,
    val username: String,
    val coin: Int,
    val birthday: String = "",
    val name: String,
    val avatar: String,
    val password: String,
    val updatedAt: String
) {
    fun getBirthdayByString(): String? {
        return try {
            val zonedDateTime =
                ZonedDateTime.parse(birthday).withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
            "${zonedDateTime.dayOfMonth}-${zonedDateTime.month.value}-${zonedDateTime.year}"
        } catch (e: Exception) {
            birthday
        }

    }
}
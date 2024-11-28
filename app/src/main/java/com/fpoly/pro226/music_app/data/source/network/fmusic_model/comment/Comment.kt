package com.fpoly.pro226.music_app.data.source.network.fmusic_model.comment

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Comment(
    val __v: Int,
    val _id: String,
    val avatar: String,
    val content: String,
    val createdAt: String,
    val id_track: String,
    val id_user: String,
    val time: String,
    val updatedAt: String,
    val username: String?
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormatDate(): String {
        val zonedDateTime = ZonedDateTime.parse(createdAt).withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
        val formatter = DateTimeFormatter.ofPattern("dd MMMM, HH:mm", Locale("vi", "VN"))
        return zonedDateTime.format(formatter)
    }
}
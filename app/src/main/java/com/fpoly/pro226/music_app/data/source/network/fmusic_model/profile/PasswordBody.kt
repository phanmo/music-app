package com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile

data class PasswordBody(
    var currentPassword: String,
    var newPassword: String,
    var confirmPassword: String
) {
    fun confirmNewPassword(): Boolean = confirmPassword == newPassword

    fun isEmptyPassword(): Boolean = confirmPassword.isBlank() || newPassword.isBlank()
}
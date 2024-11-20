package com.fpoly.pro226.music_app.data.source.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    private val ACCESS_TOKEN_KEY = "access_token"
    private val ID_USER_KEY = "id_user"

    fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, token).apply()
    }

    fun saveUser(id: String) {
        sharedPreferences.edit().putString(ID_USER_KEY, id).apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(ID_USER_KEY, null)
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    fun clearAccessToken() {
        sharedPreferences.edit().remove(ACCESS_TOKEN_KEY).apply()
    }
}

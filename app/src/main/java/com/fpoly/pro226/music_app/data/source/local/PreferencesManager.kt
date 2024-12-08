package com.fpoly.pro226.music_app.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.UserInfo
import com.google.gson.Gson

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    private val ACCESS_TOKEN_KEY = "access_token"
    private val ID_USER_KEY = "id_user"
    private val USER_KEY = "user_info"

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

    fun saveUser(user: UserInfo) {
        val jsonString = Gson().toJson(user)
        sharedPreferences.edit().putString(USER_KEY, jsonString).apply()
    }

    fun getUser(): UserInfo? {
        val userJson = sharedPreferences.getString(USER_KEY, null)
        return Gson().fromJson(userJson, UserInfo::class.java)
    }

    fun clearUserInfo() {
        sharedPreferences.edit().remove(ACCESS_TOKEN_KEY).apply()
        sharedPreferences.edit().remove(USER_KEY).apply()
    }
}

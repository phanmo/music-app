package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.User
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlayListResponse
import retrofit2.Response
import retrofit2.http.GET

const val F_MUSIC_BASE_URL = "https://music-app-be-p3hr.onrender.com/api/"

interface FMusicApiService {

    @GET("/get-list-playlist")
    suspend fun getAllPlaylist(): Response<PlayListResponse>

//
//    @GET("/login")
//    suspend fun login(): Response<User>
}

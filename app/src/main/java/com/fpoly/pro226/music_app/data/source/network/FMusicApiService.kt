package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.LoginResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.User
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlayListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

const val F_MUSIC_BASE_URL = "https://music-app-be-p3hr.onrender.com"

interface FMusicApiService {

    @GET("/api/get-list-playlist")
    suspend fun getAllPlaylist(): Response<PlayListResponse>


    @POST("/api/login")
    suspend fun login(@Body user: User): Response<LoginResponse>
}

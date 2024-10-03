package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.models.NetworkTrack
import retrofit2.Response
import retrofit2.http.GET


const val BASE_URL = "https://api.deezer.com"

interface DeezerApiService {
    @GET("/track/3135556")
    suspend fun getTrack(): Response<NetworkTrack>
}
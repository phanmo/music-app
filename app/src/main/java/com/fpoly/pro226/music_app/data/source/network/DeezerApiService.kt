package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.models.TrackApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


private const val BASE_URL = "https://api.deezer.com"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface DeezerApiService {
    @GET("/track/3135556")
    suspend fun getTrack(): Response<TrackApiModel>
}

object DeezerApi {
    val retrofitService: DeezerApiService by lazy {
        retrofit.create(DeezerApiService::class.java)
    }
}
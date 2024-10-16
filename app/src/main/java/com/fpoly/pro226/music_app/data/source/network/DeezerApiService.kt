package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.models.Album
import com.fpoly.pro226.music_app.data.source.network.models.Genres
import com.fpoly.pro226.music_app.data.source.network.models.Radios
import com.fpoly.pro226.music_app.data.source.network.models.Track
import retrofit2.Response
import retrofit2.http.GET


const val BASE_URL = "https://api.deezer.com"

interface DeezerApiService {
    @GET("/track/3135556")
    suspend fun getTrack(): Response<Track>

    @GET("album/302127")
    suspend fun getAlbum(): Response<Album>

    @GET("/genre")
    suspend fun getGenres(): Response<Genres>

    @GET("/radio")
    suspend fun getRadios(): Response<Radios>
}
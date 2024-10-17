package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.models.Album
import com.fpoly.pro226.music_app.data.source.network.models.Artists
import com.fpoly.pro226.music_app.data.source.network.models.Genres
import com.fpoly.pro226.music_app.data.source.network.models.Radios
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.data.source.network.models.Tracks
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


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

    @GET("/genre/{genreId}/artists")
    suspend fun getArtists(@Path("genreId") genreId: Int): Response<Artists>

    //https://api.deezer.com/artist/6982223/top?limit=50
    @GET("/artist/{id}/top")
    suspend fun getTracks(
        @Path("id") artistId: Int,
        @Query("limit") limit: Int = 50
    ): Response<Tracks>

}
package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.models.Album
import com.fpoly.pro226.music_app.data.source.network.models.Artists
import com.fpoly.pro226.music_app.data.source.network.models.Genres
import com.fpoly.pro226.music_app.data.source.network.models.Playlist
import com.fpoly.pro226.music_app.data.source.network.models.Playlists
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
    suspend fun getArtists(@Path("genreId") genreId: String): Response<Artists>

    // https://api.deezer.com/playlist/908622995
    @GET("/playlist/{playlistId}")
    suspend fun getPlaylist(@Path("playlistId") playlistId: String): Response<Playlist>

    //https://api.deezer.com/artist/6982223/top?limit=50
    @GET("/artist/{id}/top")
    suspend fun getTracks(
        @Path("id") artistId: String,
        @Query("limit") limit: Int = 50
    ): Response<Tracks>

    //https://api.deezer.com/radio/38305/tracks
    @GET("/radio/{id}/tracks")
    suspend fun getTracksByRadioId(
        @Path("id") radioId: String,
    ): Response<Tracks>

    //https://api.deezer.com/chart/0/tracks
    @GET("/chart/0/tracks")
    suspend fun getTracksChart(): Response<Tracks>

    //https://api.deezer.com/chart/0/artists

    @GET("/chart/0/artists")
    suspend fun getArtistsChart(): Response<Artists>

    //    https://api.deezer.com/chart/0/playlists
    @GET("/chart/0/playlists")
    suspend fun getPlaylistsChart(): Response<Playlists>

    //    https://api.deezer.com/playlist/4096400722/tracks
    @GET("/playlist/{id}/tracks")
    suspend fun getTracksByPlaylistId(
        @Path("id") playlistId: String
    ): Response<Tracks>

    //    https://api.deezer.com/search/track?q=eminem
    @GET("/search/track")
    suspend fun searchTrack(
        @Query("q") query: String
    ): Response<Tracks>
}
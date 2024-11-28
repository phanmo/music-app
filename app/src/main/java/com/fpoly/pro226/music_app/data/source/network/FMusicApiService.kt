package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.fmusic_model.coin.CoinResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.comment.CommentBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.comment.CommentResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.LoginResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.User
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.ItemPlaylistBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.ItemPlaylistResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlayListResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlaylistBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

const val F_MUSIC_BASE_URL = "https://music-app-be-p3hr.onrender.com"

interface FMusicApiService {

    @GET("/api/get-playlist/{idUser}")
    suspend fun getAllPlaylist(@Path("idUser") idUser: String): Response<PlayListResponse>

    @POST("/api/login")
    suspend fun login(@Body user: User): Response<LoginResponse>

    @POST("/api/add-playlist")
    suspend fun addPlaylist(@Body playlistBody: PlaylistBody): Response<Unit>

    @DELETE("/api/delete-playlist/{playlistId}")
    suspend fun deletePlaylist(@Path("playlistId") playlistId: String): Response<PlayListResponse>

    @GET("/api/get-list-playlist-item/{idPlaylist}")
    suspend fun getAllTrackByPlaylistId(@Path("idPlaylist") idPlaylist: String): Response<ItemPlaylistResponse>

    @GET("/api/delete-playlist-item/{idTrack}")
    suspend fun deleteItemInPlaylist(@Path("idTrack") idTrack: String): Response<ItemPlaylistResponse>

    @POST("/api/add-coin")
    suspend fun addCoin(@Body playlistBody: PlaylistBody): Response<CoinResponse>

    @GET("/api/get-coin/{idUser}")
    suspend fun getCoin(@Path("idUser") idUser: String): Response<CoinResponse>

    @POST("/api/add-playlist-item")
    suspend fun addItemToPlaylist(@Body itemPlaylistBody: ItemPlaylistBody): Response<ItemPlaylistResponse>

    @POST("/api/add-comment")
    suspend fun addComment(@Body comment: CommentBody): Response<CommentResponse>

    @GET("/api/get-comment-by-track-id/{trackId}")
    suspend fun getComments(@Path("trackId") trackId: String): Response<CommentResponse>

    @DELETE("/api/delete-comment/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: String): Response<CommentResponse>
}

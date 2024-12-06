package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.fmusic_model.coin.CoinResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.comment.CommentBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.comment.CommentResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.favorite.FavoriteBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.favorite.FavoriteResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.LoginResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.UserBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.ItemPlaylistBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.ItemPlaylistResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlayListResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlaylistBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile.PasswordBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile.ProfileResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.ranking.RankingResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

const val F_MUSIC_BASE_URL = "https://music-app-be-p3hr.onrender.com"

interface FMusicApiService {

    @GET("/api/get-playlist/{idUser}")
    suspend fun getAllPlaylist(@Path("idUser") idUser: String): Response<PlayListResponse>

    @POST("/api/login")
    suspend fun login(@Body user: UserBody): Response<LoginResponse>

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

    @GET("/api/get-users-order-by-coin")
    suspend fun getRanking(): Response<RankingResponse>


    @DELETE("/api/delete-comment/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: String): Response<CommentResponse>

    @POST("/api/add-favorite")
    suspend fun addFavorite(@Body favoriteBody: FavoriteBody): Response<Unit>

    @GET("/api/get-favorite/{userId}")
    suspend fun getFavorite(@Path("userId") userId: String): Response<FavoriteResponse>

    @DELETE("/api/delele-favorite/{id}")
    suspend fun deleteFavorite(@Path("id") id: String): Response<Unit>

    @Multipart
    @PUT("/api/edit-user-profile/{userId}")
    suspend fun updateProfileAll(
        @Path("userId") userId: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part avatar: MultipartBody.Part?
    ): Response<ProfileResponse>

    @Multipart
    @PUT("/api/edit-user-profile/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<ProfileResponse>

    @GET("/api/get-user/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): Response<ProfileResponse>


    @PUT("/api/change-password/{userId}")
    suspend fun changePassword(
        @Path("userId") userId: String,
        @Body passwordBody: PasswordBody
    ): Response<Unit>
}

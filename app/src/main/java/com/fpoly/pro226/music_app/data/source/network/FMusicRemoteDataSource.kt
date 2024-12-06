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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class FMusicRemoteDataSource(
    private val fMusicApiService: FMusicApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getPlaylist(idUser: String): Response<PlayListResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.getAllPlaylist(idUser)
        }

    suspend fun login(user: UserBody): Response<LoginResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.login(user)
        }

    suspend fun addPlaylist(playlistBody: PlaylistBody): Response<Unit> =
        withContext(ioDispatcher) {
            fMusicApiService.addPlaylist(playlistBody)
        }

    suspend fun deletePlaylist(playlistId: String): Response<PlayListResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.deletePlaylist(playlistId)
        }

    suspend fun addItemToPlaylist(itemPlaylistBody: ItemPlaylistBody): Response<ItemPlaylistResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.addItemToPlaylist(itemPlaylistBody)
        }

    suspend fun getAllTrackByPlaylistId(idPlaylist: String): Response<ItemPlaylistResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.getAllTrackByPlaylistId(idPlaylist)
        }

    suspend fun addCoin(userBody: PlaylistBody): Response<CoinResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.addCoin(userBody)
        }

    suspend fun getCoin(idUser: String): Response<CoinResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.getCoin(idUser)
        }

    suspend fun deleteItemInPlaylist(idTrack: String): Response<ItemPlaylistResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.deleteItemInPlaylist(idTrack)
        }

    suspend fun addComment(commentBody: CommentBody): Response<CommentResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.addComment(commentBody)
        }


    suspend fun getComments(trackId: String): Response<CommentResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.getComments(trackId)
        }

    suspend fun getRanking(): Response<RankingResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.getRanking()
        }

    suspend fun deleteComment(commentId: String): Response<CommentResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.deleteComment(commentId)
        }

    suspend fun getFavorite(userId: String): Response<FavoriteResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.getFavorite(userId)
        }

    suspend fun addFavorite(favoriteBody: FavoriteBody): Response<Unit> =
        withContext(ioDispatcher) {
            fMusicApiService.addFavorite(favoriteBody)
        }

    suspend fun deleteFavorite(id: String): Response<Unit> =
        withContext(ioDispatcher) {
            fMusicApiService.deleteFavorite(id)
        }

    suspend fun getProfile(id: String): Response<ProfileResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.getProfile(id)
        }

    suspend fun changePassword(userId: String, passwordBody: PasswordBody): Response<Unit> =
        withContext(ioDispatcher) {
            fMusicApiService.changePassword(userId, passwordBody)
        }

    suspend fun updateProfileAll(
        userId: String,
        data: Map<String, @JvmSuppressWildcards RequestBody>,
        avatar: MultipartBody.Part?
    ): Response<ProfileResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.updateProfileAll(userId = userId, data = data, avatar = avatar)
        }

    suspend fun updateProfile(
        userId: String,
        data: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<ProfileResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.updateProfile(userId = userId, data = data)
        }
}
package com.fpoly.pro226.music_app.data.repositories

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.fpoly.pro226.music_app.data.source.network.FMusicRemoteDataSource
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
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile.ProfileResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.ranking.RankingResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response


interface FMusicRepository {
    suspend fun getPlaylist(idUser: String): Response<PlayListResponse>
    suspend fun addPlaylist(playlistBody: PlaylistBody): Response<Unit>
    suspend fun deletePlaylist(playlistId: String): Response<PlayListResponse>
    suspend fun addCoin(userBody: PlaylistBody): Response<CoinResponse>
    suspend fun getCoin(idUser: String): Response<CoinResponse>
    suspend fun addItemToPlaylist(itemPlaylistBody: ItemPlaylistBody): Response<ItemPlaylistResponse>
    suspend fun getAllTrackByPlaylistId(idPlaylist: String): Response<ItemPlaylistResponse>
    suspend fun login(user: UserBody): Response<LoginResponse>
    suspend fun deleteComment(commentId: String): Response<CommentResponse>
    suspend fun getComments(trackId: String): Response<CommentResponse>
    suspend fun getRanking(): Response<RankingResponse>
    suspend fun addComment(commentBody: CommentBody): Response<CommentResponse>

    suspend fun deleteFavorite(id: String): Response<Unit>
    suspend fun addFavorite(favoriteBody: FavoriteBody): Response<Unit>
    suspend fun getFavorite(userId: String): Response<FavoriteResponse>
    suspend fun getProfile(id: String): Response<ProfileResponse>
    suspend fun updateProfileAll(
        userId: String,
        data: Map<String, @JvmSuppressWildcards RequestBody>,
        avatar: MultipartBody.Part?
    ): Response<ProfileResponse>

    suspend fun updateProfile(
        userId: String,
        data: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<ProfileResponse>

    val currentFavorites: SnapshotStateList<FavoriteBody>

    fun removeFavoriteLocal(trackId: String)

}

class FMusicRepositoryImpl(
    private val fMusicRemoteDataSource: FMusicRemoteDataSource,
    private val externalScope: CoroutineScope,
) : FMusicRepository {

    private val _currentFavorites = mutableStateListOf<FavoriteBody>()


    override suspend fun getPlaylist(idUser: String): Response<PlayListResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.getPlaylist(idUser)
        }.await()
    }

    override suspend fun login(user: UserBody): Response<LoginResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.login(user)
        }.await()
    }

    override suspend fun deleteComment(commentId: String): Response<CommentResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.deleteComment(commentId)
        }.await()
    }

    override suspend fun getComments(trackId: String): Response<CommentResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.getComments(trackId)
        }.await()
    }

    override suspend fun getRanking(): Response<RankingResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.getRanking()
        }.await()
    }

    override suspend fun addComment(commentBody: CommentBody): Response<CommentResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.addComment(commentBody)
        }.await()
    }

    override suspend fun deleteFavorite(id: String): Response<Unit> {
        return externalScope.async {
            fMusicRemoteDataSource.deleteFavorite(id)
        }.await()
    }

    override suspend fun addFavorite(favoriteBody: FavoriteBody): Response<Unit> {
        return externalScope.async {
            fMusicRemoteDataSource.addFavorite(favoriteBody)
        }.await()
    }

    override suspend fun getFavorite(userId: String): Response<FavoriteResponse> {
        val response = externalScope.async {
            fMusicRemoteDataSource.getFavorite(userId)
        }.await()
        if (response.isSuccessful) {
            response.body()?.let {
                _currentFavorites.clear()
                _currentFavorites.addAll(it.data)
            }
        }
        return response
    }

    override suspend fun getProfile(id: String): Response<ProfileResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.getProfile(id)
        }.await()
    }

    override suspend fun updateProfileAll(
        userId: String,
        data: Map<String, @JvmSuppressWildcards RequestBody>,
        avatar: MultipartBody.Part?
    ): Response<ProfileResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.updateProfileAll(userId, data, avatar)
        }.await()
    }

    override suspend fun updateProfile(
        userId: String,
        data: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<ProfileResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.updateProfile(userId, data)
        }.await()
    }

    override val currentFavorites: SnapshotStateList<FavoriteBody>
        get() = _currentFavorites

    override fun removeFavoriteLocal(trackId: String) {
        _currentFavorites.removeIf { it.id_track == trackId }
    }

    override suspend fun addPlaylist(playlistBody: PlaylistBody): Response<Unit> {
        return externalScope.async {
            fMusicRemoteDataSource.addPlaylist(playlistBody)
        }.await()
    }

    override suspend fun deletePlaylist(playlistId: String): Response<PlayListResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.deletePlaylist(playlistId)
        }.await()
    }

    override suspend fun addCoin(userBody: PlaylistBody): Response<CoinResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.addCoin(userBody)
        }.await()
    }

    override suspend fun getCoin(idUser: String): Response<CoinResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.getCoin(idUser)
        }.await()
    }

    override suspend fun addItemToPlaylist(itemPlaylistBody: ItemPlaylistBody): Response<ItemPlaylistResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.addItemToPlaylist(itemPlaylistBody)
        }.await()
    }

    override suspend fun getAllTrackByPlaylistId(idPlaylist: String): Response<ItemPlaylistResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.getAllTrackByPlaylistId(idPlaylist)
        }.await()
    }

}
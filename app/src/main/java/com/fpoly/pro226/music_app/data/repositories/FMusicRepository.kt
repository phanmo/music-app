package com.fpoly.pro226.music_app.data.repositories

import com.fpoly.pro226.music_app.data.source.network.FMusicRemoteDataSource
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.coin.CoinResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.comment.CommentBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.comment.CommentResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.LoginResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.User
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.ItemPlaylistBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.ItemPlaylistResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlayListResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlaylistBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.ranking.RankingResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import retrofit2.Response


interface FMusicRepository {
    suspend fun getPlaylist(idUser: String): Response<PlayListResponse>
    suspend fun addPlaylist(playlistBody: PlaylistBody): Response<Unit>
    suspend fun deletePlaylist(playlistId: String): Response<PlayListResponse>
    suspend fun addCoin(userBody: PlaylistBody): Response<CoinResponse>
    suspend fun getCoin(idUser: String): Response<CoinResponse>
    suspend fun addItemToPlaylist(itemPlaylistBody: ItemPlaylistBody): Response<ItemPlaylistResponse>
    suspend fun getAllTrackByPlaylistId(idPlaylist: String): Response<ItemPlaylistResponse>
    suspend fun login(user: User): Response<LoginResponse>
    suspend fun deleteComment(commentId: String): Response<CommentResponse>
    suspend fun getComments(trackId: String): Response<CommentResponse>
    suspend fun getRanking(): Response<RankingResponse>
    suspend fun addComment(commentBody: CommentBody): Response<CommentResponse>

}

class FMusicRepositoryImpl(
    private val fMusicRemoteDataSource: FMusicRemoteDataSource,
    private val externalScope: CoroutineScope,
) : FMusicRepository {

    override suspend fun getPlaylist(idUser: String): Response<PlayListResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.getPlaylist(idUser)
        }.await()
    }

    override suspend fun login(user: User): Response<LoginResponse> {
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
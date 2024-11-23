package com.fpoly.pro226.music_app.data.repositories

import com.fpoly.pro226.music_app.data.source.network.FMusicRemoteDataSource
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.LoginResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.User
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.coin.CoinResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.ItemPlaylistResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.ItemPlaylistBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlayListResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlaylistBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import retrofit2.Response


interface FMusicRepository {
    suspend fun getPlaylist(idUser: String): Response<PlayListResponse>
    suspend fun addPlaylist(playlistBody: PlaylistBody): Response<Unit>
    suspend fun addCoin(userBody: PlaylistBody): Response<CoinResponse>
    suspend fun getCoin(idUser: String): Response<CoinResponse>
    suspend fun addItemToPlaylist(itemPlaylistBody: ItemPlaylistBody): Response<ItemPlaylistResponse>
    suspend fun getAllTrackByPlaylistId(idPlaylist: String): Response<ItemPlaylistResponse>
    suspend fun login(user: User): Response<LoginResponse>

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

    override suspend fun addPlaylist(playlistBody: PlaylistBody): Response<Unit> {
        return externalScope.async {
            fMusicRemoteDataSource.addPlaylist(playlistBody)
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
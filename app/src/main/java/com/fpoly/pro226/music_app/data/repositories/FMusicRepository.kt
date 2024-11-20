package com.fpoly.pro226.music_app.data.repositories

import com.fpoly.pro226.music_app.data.source.network.FMusicRemoteDataSource
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.LoginResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.User
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlayListResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlaylistBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import retrofit2.Response


interface FMusicRepository {
    suspend fun getPlaylist(idUser: String): Response<PlayListResponse>
    suspend fun addPlaylist(playlistBody: PlaylistBody): Response<Unit>
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

}
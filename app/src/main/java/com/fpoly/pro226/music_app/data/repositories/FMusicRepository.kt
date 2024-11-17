package com.fpoly.pro226.music_app.data.repositories

import com.fpoly.pro226.music_app.data.source.network.FMusicRemoteDataSource
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.LoginResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.User
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlayListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import retrofit2.Response


interface FMusicRepository {
    suspend fun getPlaylist(): Response<PlayListResponse>
    suspend fun login(user: User): Response<LoginResponse>

}

class FMusicRepositoryImpl(
    private val fMusicRemoteDataSource: FMusicRemoteDataSource,
    private val externalScope: CoroutineScope,
) : FMusicRepository {

    override suspend fun getPlaylist(): Response<PlayListResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.getPlaylist()
        }.await()
    }

    override suspend fun login(user: User): Response<LoginResponse> {
        return externalScope.async {
            fMusicRemoteDataSource.login(user)
        }.await()
    }

}
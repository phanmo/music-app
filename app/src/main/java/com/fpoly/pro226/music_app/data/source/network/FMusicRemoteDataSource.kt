package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.LoginResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.User
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlayListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

class FMusicRemoteDataSource(
    private val fMusicApiService: FMusicApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getPlaylist(): Response<PlayListResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.getAllPlaylist()
        }

    suspend fun login(user: User): Response<LoginResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.login(user)
        }

}
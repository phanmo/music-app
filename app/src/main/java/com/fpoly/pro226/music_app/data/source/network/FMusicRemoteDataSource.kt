package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.LoginResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.User
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlayListResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist.PlaylistBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

class FMusicRemoteDataSource(
    private val fMusicApiService: FMusicApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getPlaylist(idUser: String): Response<PlayListResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.getAllPlaylist(idUser)
        }

    suspend fun login(user: User): Response<LoginResponse> =
        withContext(ioDispatcher) {
            fMusicApiService.login(user)
        }

    suspend fun addPlaylist(playlistBody: PlaylistBody): Response<Unit> =
        withContext(ioDispatcher) {
            fMusicApiService.addPlaylist(playlistBody)
        }

}
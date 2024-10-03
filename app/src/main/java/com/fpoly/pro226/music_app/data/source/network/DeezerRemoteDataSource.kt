package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.models.NetworkTrack
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

class DeezerRemoteDataSource(
    private val deezerApiService: DeezerApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getTrack(): Response<NetworkTrack> =
        withContext(ioDispatcher) {
            deezerApiService.getTrack()
        }

}
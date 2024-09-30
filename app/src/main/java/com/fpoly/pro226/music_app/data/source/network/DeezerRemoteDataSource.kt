package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.models.TrackApiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

class DeezerRemoteDataSource(
    private val deezerApiService: DeezerApiService = DeezerApi.retrofitService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getTrack(): Response<TrackApiModel> =
        withContext(ioDispatcher) {
            deezerApiService.getTrack()
        }

}
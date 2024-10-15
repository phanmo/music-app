package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.models.Album
import com.fpoly.pro226.music_app.data.source.network.models.Track
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

class DeezerRemoteDataSource(
    private val deezerApiService: DeezerApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getTrack(): Response<Track> =
        withContext(ioDispatcher) {
            deezerApiService.getTrack()
        }
    suspend fun getAlbum(): Response<Album> =
        withContext(ioDispatcher) {
            deezerApiService.getAlbum()
        }
}
package com.fpoly.pro226.music_app.data.repositories

import com.fpoly.pro226.music_app.data.source.network.DeezerRemoteDataSource
import com.fpoly.pro226.music_app.data.source.network.models.TrackApiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Response

class DeezerRepository(
    private val deezerRemoteDataSource: DeezerRemoteDataSource,
    private val externalScope: CoroutineScope

) {

    private val lastTrackMutex = Mutex()
    private var lastTrackResponse: Response<TrackApiModel>? = null
    suspend fun getTrack(refresh: Boolean = false): Response<TrackApiModel>? {
        return if (refresh) {
            externalScope.async {
                deezerRemoteDataSource.getTrack().also {
                    lastTrackMutex.withLock {
                        lastTrackResponse = it
                    }
                }
            }.await()
        } else {
            return lastTrackMutex.withLock { this.lastTrackResponse }
        }
    }
}
package com.fpoly.pro226.music_app.data.repositories

import com.fpoly.pro226.music_app.data.source.network.DeezerRemoteDataSource
import com.fpoly.pro226.music_app.data.source.network.models.NetworkTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Response

interface DeezerRepository {
    suspend fun getTrack(refresh: Boolean = false): Response<NetworkTrack>?
}

class DeezerRepositoryImpl(
    private val deezerRemoteDataSource: DeezerRemoteDataSource,
    private val externalScope: CoroutineScope,
) : DeezerRepository {
    private val lastTrackMutex = Mutex()
    private var lastTrackResponse: Response<NetworkTrack>? = null
    override suspend fun getTrack(refresh: Boolean): Response<NetworkTrack>? {
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
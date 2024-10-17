package com.fpoly.pro226.music_app.data.repositories

import com.fpoly.pro226.music_app.data.source.network.DeezerRemoteDataSource
import com.fpoly.pro226.music_app.data.source.network.models.Album
import com.fpoly.pro226.music_app.data.source.network.models.Artists
import com.fpoly.pro226.music_app.data.source.network.models.Genres
import com.fpoly.pro226.music_app.data.source.network.models.Radios
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.data.source.network.models.Tracks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Response

interface DeezerRepository {
    suspend fun getTrack(refresh: Boolean = false): Response<Track>?
    suspend fun getGenres(): Response<Genres>?
    suspend fun getRadios(): Response<Radios>?
    suspend fun getAlbum(): Response<Album>?
    suspend fun getArtists(genreId: Int): Response<Artists>?
    suspend fun getTracks(artistId: Int): Response<Tracks>?
}

class DeezerRepositoryImpl(
    private val deezerRemoteDataSource: DeezerRemoteDataSource,
    private val externalScope: CoroutineScope,
) : DeezerRepository {
    private val lastTrackMutex = Mutex()
    private var lastTrackResponse: Response<Track>? = null
    override suspend fun getTrack(refresh: Boolean): Response<Track>? {
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

    override suspend fun getGenres(): Response<Genres> {
        return externalScope.async {
            deezerRemoteDataSource.getGenres()
        }.await()
    }

    override suspend fun getRadios(): Response<Radios> {
        return externalScope.async {
            deezerRemoteDataSource.getRadios()
        }.await()
    }

    override suspend fun getAlbum(): Response<Album> {
        return externalScope.async {
            deezerRemoteDataSource.getAlbum()
        }.await()
    }

    override suspend fun getArtists(genreId: Int): Response<Artists> {
        return externalScope.async {
            deezerRemoteDataSource.getArtists(genreId)
        }.await()
    }

    override suspend fun getTracks(artistId: Int): Response<Tracks> {
        return externalScope.async {
            deezerRemoteDataSource.getTracks(artistId)
        }.await()
    }
}
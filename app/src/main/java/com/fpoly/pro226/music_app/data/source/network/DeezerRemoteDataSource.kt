package com.fpoly.pro226.music_app.data.source.network

import com.fpoly.pro226.music_app.data.source.network.models.Album
import com.fpoly.pro226.music_app.data.source.network.models.Artists
import com.fpoly.pro226.music_app.data.source.network.models.Genres
import com.fpoly.pro226.music_app.data.source.network.models.Radios
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.data.source.network.models.Tracks
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

    suspend fun getGenres(): Response<Genres> =
        withContext(ioDispatcher) {
            deezerApiService.getGenres()
        }

    suspend fun getRadios(): Response<Radios> =
        withContext(ioDispatcher) {
            deezerApiService.getRadios()
        }

    suspend fun getArtists(genreId: String): Response<Artists> =
        withContext(ioDispatcher) {
            deezerApiService.getArtists(genreId)
        }

    suspend fun getTracks(artistId: String): Response<Tracks> =
        withContext(ioDispatcher) {
            deezerApiService.getTracks(artistId)
        }

    suspend fun getTracksByRadioId(radioId: String): Response<Tracks> =
        withContext(ioDispatcher) {
            deezerApiService.getTracksByRadioId(radioId)
        }
}
package com.fpoly.pro226.music_app.data.repositories

import com.fpoly.pro226.music_app.data.source.network.DeezerRemoteDataSource
import com.fpoly.pro226.music_app.data.source.network.models.TrackApiModel
import retrofit2.Response

class DeezerRepository(
    private val deezerRemoteDataSource: DeezerRemoteDataSource

) {
    suspend fun getTrack(): Response<TrackApiModel> = deezerRemoteDataSource.getTrack()
}
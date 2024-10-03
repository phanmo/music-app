package com.fpoly.pro226.music_app.utilities.components.di

import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.data.repositories.DeezerRepositoryImpl
import com.fpoly.pro226.music_app.data.source.network.BASE_URL
import com.fpoly.pro226.music_app.data.source.network.DeezerApiService
import com.fpoly.pro226.music_app.data.source.network.DeezerRemoteDataSource
import com.fpoly.pro226.music_app.utilities.components.factory.SongViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class AppContainer(externalScope: CoroutineScope) {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: DeezerApiService by lazy {
        retrofit.create(DeezerApiService::class.java)
    }

    private val deezerRemoteDataSource = DeezerRemoteDataSource(retrofitService, Dispatchers.IO)

    private val deezerRepository: DeezerRepository =
        DeezerRepositoryImpl(deezerRemoteDataSource, externalScope)

    val songViewModelFactory = SongViewModelFactory(deezerRepository)
}
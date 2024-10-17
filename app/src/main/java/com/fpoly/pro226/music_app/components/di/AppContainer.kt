package com.fpoly.pro226.music_app.components.di

import com.fpoly.pro226.music_app.components.factory.ExploreViewModelFactory
import com.fpoly.pro226.music_app.components.factory.GenreViewModelFactory
import com.fpoly.pro226.music_app.components.factory.SongViewModelFactory
import com.fpoly.pro226.music_app.components.factory.TrackViewModelFactory
import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.data.repositories.DeezerRepositoryImpl
import com.fpoly.pro226.music_app.data.source.network.BASE_URL
import com.fpoly.pro226.music_app.data.source.network.DeezerApiService
import com.fpoly.pro226.music_app.data.source.network.DeezerRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(externalScope: CoroutineScope) {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideHttpClient())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: DeezerApiService by lazy {
        retrofit.create(DeezerApiService::class.java)
    }

    private val deezerRemoteDataSource = DeezerRemoteDataSource(retrofitService, Dispatchers.IO)

    private val deezerRepository: DeezerRepository =
        DeezerRepositoryImpl(deezerRemoteDataSource, externalScope)

    val songViewModelFactory = SongViewModelFactory(deezerRepository)
    val exploreViewModelFactory = ExploreViewModelFactory(deezerRepository)
    val genreViewModelFactory = GenreViewModelFactory(deezerRepository)
    val trackViewModelFactory = TrackViewModelFactory(deezerRepository)

    private fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .connectTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200, TimeUnit.SECONDS).addInterceptor(logging).build()
    }
}
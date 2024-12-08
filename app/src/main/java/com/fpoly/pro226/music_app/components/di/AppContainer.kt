package com.fpoly.pro226.music_app.components.di

import com.fpoly.pro226.music_app.components.factory.GenreViewModelFactory
import com.fpoly.pro226.music_app.data.repositories.DeezerRepository
import com.fpoly.pro226.music_app.data.repositories.DeezerRepositoryImpl
import com.fpoly.pro226.music_app.data.repositories.FMusicRepository
import com.fpoly.pro226.music_app.data.repositories.FMusicRepositoryImpl
import com.fpoly.pro226.music_app.data.source.network.BASE_URL
import com.fpoly.pro226.music_app.data.source.network.DeezerApiService
import com.fpoly.pro226.music_app.data.source.network.DeezerRemoteDataSource
import com.fpoly.pro226.music_app.data.source.network.FMusicApiService
import com.fpoly.pro226.music_app.data.source.network.FMusicRemoteDataSource
import com.fpoly.pro226.music_app.data.source.network.F_MUSIC_BASE_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(externalScope: CoroutineScope) {

    private val fMusicRetrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideHttpClient())
        .baseUrl(F_MUSIC_BASE_URL)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideHttpClient())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: DeezerApiService by lazy {
        retrofit.create(DeezerApiService::class.java)
    }

    private val fMusicRetrofitService: FMusicApiService by lazy {
        fMusicRetrofit.create(FMusicApiService::class.java)
    }

    private val deezerRemoteDataSource = DeezerRemoteDataSource(retrofitService, Dispatchers.IO)
    private val fMusicRemoteDataSource =
        FMusicRemoteDataSource(fMusicRetrofitService, Dispatchers.IO)

    val deezerRepository: DeezerRepository =
        DeezerRepositoryImpl(deezerRemoteDataSource, externalScope)

    val fMusicRepository: FMusicRepository =
        FMusicRepositoryImpl(fMusicRemoteDataSource, externalScope)


    val genreViewModelFactory = GenreViewModelFactory(deezerRepository)

    private fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .connectTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200, TimeUnit.SECONDS).addInterceptor(logging).build()
    }
}
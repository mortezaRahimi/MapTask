package com.example.holloomap.data.di

import com.example.holloomap.data.remote.MapApiService
import com.example.holloomap.data.remote.UrlConstant.BASE_URL
import com.example.holloomap.data.repository.MapRepository
import com.example.holloomap.domain.use_case.GetDirection
import com.example.holloomap.domain.use_case.MapUseCase
import com.example.holloomap.data.repository.MapRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideMapApiService(client: OkHttpClient): MapApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(MapApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMapRepository(
        api: MapApiService
    ): MapRepository {
        return MapRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideMapUseCases(
        repository: MapRepository
    ): MapUseCase {
        return MapUseCase(
            getDirection = GetDirection(repository)
        )
    }
}
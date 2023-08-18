package com.example.holloomap.data.di

import android.app.Application
import com.example.holloomap.data.repository.location.DefaultLocationTracker
import com.example.holloomap.data.remote.MapApiService
import com.example.holloomap.data.remote.UrlConstant.BASE_URL
import com.example.holloomap.data.repository.MapRepository
import com.example.holloomap.domain.use_case.GetDirection
import com.example.holloomap.domain.use_case.MapUseCase
import com.example.holloomap.data.repository.MapRepositoryImpl
import com.example.holloomap.domain.use_case.location.LocationTracker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(
        application: Application
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    @Provides
    @Singleton
    fun providesLocationTracker(
        fusedLocationProviderClient: FusedLocationProviderClient,
        application: Application
    ): LocationTracker = DefaultLocationTracker(
        fusedLocationProviderClient = fusedLocationProviderClient,
        application = application
    )
}
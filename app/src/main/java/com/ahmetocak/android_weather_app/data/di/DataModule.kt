package com.ahmetocak.android_weather_app.data.di

import com.ahmetocak.android_weather_app.data.WeatherRepository
import com.ahmetocak.android_weather_app.data.WeatherRepositoryImpl
import com.ahmetocak.android_weather_app.data.remote.RemoteDataSource
import com.ahmetocak.android_weather_app.data.remote.RemoteDataSourceImpl
import com.ahmetocak.android_weather_app.data.remote.api.OpenWeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        api: OpenWeatherApi,
        @Dispatcher(AppDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): RemoteDataSource {
        return RemoteDataSourceImpl(api, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideWeatherRepository(remoteDataSource: RemoteDataSource): WeatherRepository {
        return WeatherRepositoryImpl(remoteDataSource)
    }
}
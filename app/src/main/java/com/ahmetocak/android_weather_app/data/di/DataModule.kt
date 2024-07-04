package com.ahmetocak.android_weather_app.data.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.ahmetocak.android_weather_app.data.WeatherRepository
import com.ahmetocak.android_weather_app.data.WeatherRepositoryImpl
import com.ahmetocak.android_weather_app.data.local.LocalPreferenceManagerImpl
import com.ahmetocak.android_weather_app.data.local.LocationPreferenceManager
import com.ahmetocak.android_weather_app.data.remote.RemoteDataSource
import com.ahmetocak.android_weather_app.data.remote.RemoteDataSourceImpl
import com.ahmetocak.android_weather_app.data.remote.api.OpenWeatherApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(application: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(application)
    }

    @Singleton
    @Provides
    fun provideDatastore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("app_preferences_file")
            }
        )
    }

    @Singleton
    @Provides
    fun provideLocalPreferenceManager(dataStore: DataStore<Preferences>): LocationPreferenceManager {
        return LocalPreferenceManagerImpl(dataStore)
    }
}
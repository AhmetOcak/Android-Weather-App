package com.ahmetocak.android_weather_app.data

import com.ahmetocak.android_weather_app.data.remote.RemoteDataSource
import com.ahmetocak.android_weather_app.data.remote.model.toWeatherForecastModel
import com.ahmetocak.android_weather_app.data.remote.model.toWeatherModel
import com.ahmetocak.android_weather_app.model.BaseResponse
import com.ahmetocak.android_weather_app.model.WeatherForecastModel
import com.ahmetocak.android_weather_app.model.WeatherModel
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : WeatherRepository {
    override suspend fun getCurrentWeatherData(
        latitude: Double,
        longitude: Double
    ): BaseResponse<WeatherModel> {
        return remoteDataSource.getCurrentWeatherData(latitude, longitude)
            .mapResponse { it.toWeatherModel() }
    }

    override suspend fun getWeatherForecastData(
        latitude: Double,
        longitude: Double
    ): BaseResponse<WeatherForecastModel> {
        return remoteDataSource.getWeatherForecastData(latitude, longitude)
            .mapResponse { it.toWeatherForecastModel() }
    }

    private fun <I : Any, O : Any> BaseResponse<I>.mapResponse(mapper: (I) -> O): BaseResponse<O> {
        return when (this) {
            is BaseResponse.Success -> {
                BaseResponse.Success(mapper(this.data))
            }

            is BaseResponse.Error -> {
                BaseResponse.Error(exception)
            }
        }
    }
}
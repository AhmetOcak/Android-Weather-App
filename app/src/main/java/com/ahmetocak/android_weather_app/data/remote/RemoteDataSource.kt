package com.ahmetocak.android_weather_app.data.remote

import com.ahmetocak.android_weather_app.data.remote.model.CurrentWeatherDto
import com.ahmetocak.android_weather_app.data.remote.model.WeatherForecastDto
import com.ahmetocak.android_weather_app.model.BaseResponse

interface RemoteDataSource {
    suspend fun getCurrentWeatherData(
        latitude: Double,
        longitude: Double
    ): BaseResponse<CurrentWeatherDto>

    suspend fun getWeatherForecastData(
        latitude: Double,
        longitude: Double
    ): BaseResponse<WeatherForecastDto>
}
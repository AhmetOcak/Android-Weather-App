package com.ahmetocak.android_weather_app.data

import com.ahmetocak.android_weather_app.model.BaseResponse
import com.ahmetocak.android_weather_app.model.WeatherForecastModel
import com.ahmetocak.android_weather_app.model.WeatherModel

interface WeatherRepository {
    suspend fun getCurrentWeatherData(
        latitude: Double,
        longitude: Double
    ): BaseResponse<WeatherModel>

    suspend fun getWeatherForecastData(
        latitude: Double,
        longitude: Double
    ): BaseResponse<WeatherForecastModel>
}
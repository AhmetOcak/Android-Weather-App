package com.ahmetocak.android_weather_app.data.remote

import com.ahmetocak.android_weather_app.data.remote.model.WeatherModelDto
import com.ahmetocak.android_weather_app.model.BaseResponse

interface RemoteDataSource {
    suspend fun getCurrentWeatherData(
        latitude: Double,
        longitude: Double
    ): BaseResponse<WeatherModelDto>
}
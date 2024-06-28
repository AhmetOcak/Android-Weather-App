package com.ahmetocak.android_weather_app.data.remote.api

import com.ahmetocak.android_weather_app.BuildConfig
import com.ahmetocak.android_weather_app.data.remote.model.CurrentWeatherDto
import com.ahmetocak.android_weather_app.data.remote.model.WeatherForecastDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ): Response<CurrentWeatherDto>

    @GET("/data/2.5/forecast")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ): Response<WeatherForecastDto>
}
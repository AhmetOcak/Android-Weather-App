package com.ahmetocak.android_weather_app.data.remote.model

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    val id: Int,
    val main: String,
    val description: String
)

data class MainDto(
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double
)

data class WindDto(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

data class CloudsDto(
    @SerializedName("all")
    val cloudiness: Int
)
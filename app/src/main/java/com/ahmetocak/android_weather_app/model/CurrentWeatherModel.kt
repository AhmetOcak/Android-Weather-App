package com.ahmetocak.android_weather_app.model

data class WeatherModel(
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val sun: Sun,
    val cityName: String
)

data class Sun(
    val country: String,
    val sunrise: Int,
    val sunset: Int
)
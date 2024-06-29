package com.ahmetocak.android_weather_app.feature.home

data class CurrentWeatherInfo(
    val currentTemp: String,
    val mainDescription: String,
    val description: String,
    val feelsLike: String,
    val minTemp: String,
    val maxTemp: String,
    val cityAndCountry: String,
    val isNight: Boolean
)

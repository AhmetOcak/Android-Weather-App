package com.ahmetocak.android_weather_app.ui

data class ItemDailyForecastModel(
    val day: String,
    val mainDescription: String,
    val description: String,
    val weatherDate: WeatherDate,
    val minTemp: String,
    val maxTemp: String
)

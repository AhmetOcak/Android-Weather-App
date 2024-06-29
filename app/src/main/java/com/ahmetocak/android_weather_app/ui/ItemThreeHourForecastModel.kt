package com.ahmetocak.android_weather_app.ui

data class ItemThreeHourForecastModel(
    val temp: String,
    val weatherDate: WeatherDate,
    val mainDescription: String,
    val description: String
)

data class WeatherDate(
    val hour: String,
    val day: String,
    val isDayNight: Boolean
)

package com.ahmetocak.android_weather_app.model

data class WeatherForecastModel(
    val weather: List<WeatherList>,
    val city: City
)

data class WeatherList(
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val pop: Double,
    val date: Long
)

data class City(
    val name: String,
    val country: String,
    val sunrise: Int,
    val sunset: Int
)
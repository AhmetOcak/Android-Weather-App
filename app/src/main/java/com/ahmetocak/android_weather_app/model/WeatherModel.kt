package com.ahmetocak.android_weather_app.model

data class WeatherModel(
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val sun: Sun,
    val cityName: String
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

data class Clouds(
    val cloudiness: Int
)

data class Sun(
    val country: String,
    val sunrise: Int,
    val sunset: Int
)
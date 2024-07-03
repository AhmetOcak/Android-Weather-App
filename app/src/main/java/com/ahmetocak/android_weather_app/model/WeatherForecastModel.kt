package com.ahmetocak.android_weather_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherForecastModel(
    val weather: List<WeatherList>,
    val city: City
): Parcelable

@Parcelize
data class WeatherList(
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val pop: Double,
    val date: Long
): Parcelable

@Parcelize
data class City(
    val name: String,
    val country: String,
    val sunrise: Long,
    val sunset: Long
): Parcelable
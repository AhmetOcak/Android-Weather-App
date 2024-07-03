package com.ahmetocak.android_weather_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    val id: Int,
    val main: String,
    val description: String
): Parcelable

@Parcelize
data class Main(
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double
): Parcelable


@Parcelize
data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
): Parcelable


@Parcelize
data class Clouds(
    val cloudiness: Int
): Parcelable

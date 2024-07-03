package com.ahmetocak.android_weather_app.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentWeatherInfo(
    val currentTemp: String,
    val mainDescription: String,
    val description: String,
    val feelsLike: String,
    val minTemp: String,
    val maxTemp: String,
    val cityAndCountry: String,
    val isNight: Boolean
) : Parcelable
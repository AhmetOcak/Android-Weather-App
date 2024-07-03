package com.ahmetocak.android_weather_app.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class ItemDailyForecastModelList(
    val itemDailyForecastModel: List<ItemDailyForecastModel>
): Parcelable

@Parcelize
data class ItemDailyForecastModel(
    val id: Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
    val mainDescription: String,
    val description: String,
    val weatherDate: WeatherDate,
    val minTemp: String,
    val maxTemp: String
): Parcelable

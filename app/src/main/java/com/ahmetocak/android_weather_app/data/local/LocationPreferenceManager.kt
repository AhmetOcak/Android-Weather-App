package com.ahmetocak.android_weather_app.data.local

import android.location.Location

interface LocationPreferenceManager {

    suspend fun getLocation(): Location?
    suspend fun saveLocation(latitude: Double, longitude: Double)
}
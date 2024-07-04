package com.ahmetocak.android_weather_app.data

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import java.lang.Exception
import javax.inject.Inject

class LocationTracker @Inject constructor(private val client: FusedLocationProviderClient) {
    @SuppressLint("MissingPermission")
    fun getLocation(onSuccess: (Location?) -> Unit, onFailure: (Exception?) -> Unit) {
        client.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(task.result)
            } else {
                onFailure(task.exception)
            }
        }
    }
}
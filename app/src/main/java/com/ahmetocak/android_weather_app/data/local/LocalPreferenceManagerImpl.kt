package com.ahmetocak.android_weather_app.data.local

import android.location.Location
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import com.ahmetocak.android_weather_app.data.local.LocalPreferenceManagerImpl.PreferenceKeys.LATITUDE
import com.ahmetocak.android_weather_app.data.local.LocalPreferenceManagerImpl.PreferenceKeys.LONGITUDE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalPreferenceManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): LocationPreferenceManager {

    private object PreferenceKeys {
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
    }

    override suspend fun getLocation(): Location? {
        val latitude =  dataStore.data.map { preferences ->
            preferences[LATITUDE]
        }.first()
        val longitude =  dataStore.data.map { preferences ->
            preferences[LONGITUDE]
        }.first()

        return if (latitude != null && longitude != null) {
            Location("provider").apply {
                this.latitude = latitude
                this.longitude = longitude
            }
        } else null
    }

    override suspend fun saveLocation(latitude: Double, longitude: Double) {
        dataStore.edit { preferences ->
            preferences[LATITUDE] = latitude
            preferences[LONGITUDE] = longitude
        }
    }
}
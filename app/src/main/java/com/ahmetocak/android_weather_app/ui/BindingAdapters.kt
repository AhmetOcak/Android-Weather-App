package com.ahmetocak.android_weather_app.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("mainDescription", "description", "isDayNight")
fun setWeatherStatusImage(
    view: ImageView,
    mainDescription: String?,
    description: String?,
    isDayNight: Boolean?
) {
    if (mainDescription != null && description != null && isDayNight != null) {
        view.setImageResource(
            WeatherType.setWeatherImage(
                mainDescription = mainDescription,
                description = description,
                isDayNight = isDayNight
            )
        )
    }
}
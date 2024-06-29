package com.ahmetocak.android_weather_app.ui

import androidx.annotation.DrawableRes
import com.ahmetocak.android_weather_app.R

sealed class WeatherType(
    val weatherDescription: String,
    @DrawableRes val id: Int
) {
    data object ClearSkyDay : WeatherType(
        weatherDescription = WeatherConditions.CLEAR_SKY,
        id = R.drawable.clear_sky_day
    )

    data object ClearSkyNight : WeatherType(
        weatherDescription = WeatherConditions.CLEAR_SKY,
        id = R.drawable.clear_sky_night
    )

    data object FewCloudsDay : WeatherType(
        weatherDescription = WeatherConditions.FEW_CLOUDS,
        id = R.drawable.few_clouds_day
    )

    data object FewCloudsNight : WeatherType(
        weatherDescription = WeatherConditions.FEW_CLOUDS,
        id = R.drawable.clear_sky_night
    )

    data object ScatteredClouds : WeatherType(
        weatherDescription = WeatherConditions.SCATTERED_CLOUDS,
        id = R.drawable.scattered_clouds
    )

    data object BrokenCloudsDay : WeatherType(
        weatherDescription = WeatherConditions.BROKEN_CLOUDS,
        id = R.drawable.broken_clouds_day
    )

    data object BrokenCloudsNight : WeatherType(
        weatherDescription = WeatherConditions.BROKEN_CLOUDS,
        id = R.drawable.broken_clouds_night
    )

    data object ShowerRainDay : WeatherType(
        weatherDescription = WeatherConditions.SHOWER_RAIN,
        id = R.drawable.rain_day
    )

    data object ShowerRainNight : WeatherType(
        weatherDescription = WeatherConditions.SHOWER_RAIN,
        id = R.drawable.rain_night
    )

    data object RainDay : WeatherType(
        weatherDescription = WeatherConditions.RAIN,
        id = R.drawable.rain_day
    )

    data object RainNight : WeatherType(
        weatherDescription = WeatherConditions.RAIN,
        id = R.drawable.rain_day
    )

    data object Thunderstorm : WeatherType(
        weatherDescription = WeatherConditions.THUNDERSTORM,
        id = R.drawable.thunderstorrm
    )

    data object Snow : WeatherType(
        weatherDescription = WeatherConditions.SNOW,
        id = R.drawable.snow
    )

    data object MistDay : WeatherType(
        weatherDescription = WeatherConditions.MIST,
        id = R.drawable.mist_day
    )

    data object MistNight : WeatherType(
        weatherDescription = WeatherConditions.MIST,
        id = R.drawable.mist_night
    )

    companion object {
        fun setWeatherImage(
            mainDescription: String,
            description: String,
            isDayNight: Boolean
        ): Int {
            when (mainDescription) {
                MainWeatherConditions.CLOUDS -> {
                    return if (description == ScatteredClouds.weatherDescription) {
                        ScatteredClouds.id
                    } else if (description == FewCloudsDay.weatherDescription) {
                        if (isDayNight) {
                            FewCloudsNight.id
                        } else {
                            FewCloudsDay.id
                        }
                    } else {
                        if (isDayNight) {
                            BrokenCloudsNight.id
                        } else {
                            BrokenCloudsDay.id
                        }
                    }
                }
                MainWeatherConditions.RAIN -> {
                    return if (description == ShowerRainDay.weatherDescription) {
                        if (isDayNight) {
                            ShowerRainNight.id
                        } else {
                            ShowerRainDay.id
                        }
                    } else {
                        if (isDayNight) {
                            RainNight.id
                        } else {
                            RainDay.id
                        }
                    }
                }
                MainWeatherConditions.THUNDERSTORM -> {
                    return Thunderstorm.id
                }
                MainWeatherConditions.SNOW -> {
                    return Snow.id
                }
                MainWeatherConditions.CLEAR -> {
                    return if (isDayNight) {
                        ClearSkyNight.id
                    } else {
                        ClearSkyDay.id
                    }
                }
                else -> {
                    return if (isDayNight) {
                        MistNight.id
                    } else {
                        MistDay.id
                    }
                }
            }
        }
    }
}

object WeatherConditions {
    const val CLEAR_SKY = "clear sky"
    const val FEW_CLOUDS = "few clouds"
    const val SCATTERED_CLOUDS = "scattered clouds"
    const val BROKEN_CLOUDS = "broken clouds"
    const val SHOWER_RAIN = "shower rain"
    const val RAIN = "rain"
    const val THUNDERSTORM = "thunderstorm"
    const val SNOW = "snow"
    const val MIST = "mist"
}

object MainWeatherConditions {
    const val CLOUDS = "Clouds"
    const val SNOW = "Snow"
    const val RAIN = "Rain"
    const val THUNDERSTORM = "Thunderstorm"
    const val CLEAR = "Clear"
}
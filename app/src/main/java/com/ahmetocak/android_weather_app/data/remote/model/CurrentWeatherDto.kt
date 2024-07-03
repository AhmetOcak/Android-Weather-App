package com.ahmetocak.android_weather_app.data.remote.model

import com.ahmetocak.android_weather_app.model.Clouds
import com.ahmetocak.android_weather_app.model.Main
import com.ahmetocak.android_weather_app.model.Sun
import com.ahmetocak.android_weather_app.model.Weather
import com.ahmetocak.android_weather_app.model.WeatherModel
import com.ahmetocak.android_weather_app.model.Wind
import com.google.gson.annotations.SerializedName

data class CurrentWeatherDto(
    val weather: List<WeatherDto>,
    val main: MainDto,
    val wind: WindDto,
    val clouds: CloudsDto,

    @SerializedName("sys")
    val sun: SunDto,

    @SerializedName("name")
    val cityName: String,

    @SerializedName("dt")
    val date: Long
)

data class SunDto(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

fun CurrentWeatherDto.toWeatherModel(): WeatherModel {
    return WeatherModel(
        weather = weather.map {
            Weather(
                id = it.id,
                main = it.main,
                description = it.description
            )
        },
        main = Main(
            temp = main.temp,
            tempMax = main.tempMax,
            tempMin = main.tempMin,
            pressure = main.pressure,
            humidity = main.humidity,
            feelsLike = main.feelsLike
        ),
        wind = Wind(
            speed = wind.speed,
            deg = wind.deg,
            gust = wind.gust
        ),
        clouds = Clouds(cloudiness = clouds.cloudiness),
        sun = Sun(
            country = sun.country,
            sunrise = sun.sunrise,
            sunset = sun.sunset
        ),
        cityName = cityName,
        date = date
    )
}
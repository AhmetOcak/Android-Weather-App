package com.ahmetocak.android_weather_app.data.remote.model

import com.ahmetocak.android_weather_app.model.Clouds
import com.ahmetocak.android_weather_app.model.Main
import com.ahmetocak.android_weather_app.model.Sun
import com.ahmetocak.android_weather_app.model.Weather
import com.ahmetocak.android_weather_app.model.WeatherModel
import com.ahmetocak.android_weather_app.model.Wind
import com.google.gson.annotations.SerializedName

data class WeatherModelDto(
    val weather: List<WeatherDto>,
    val main: MainDto,
    val wind: WindDto,
    val clouds: CloudsDto,
    @SerializedName("sys")
    val sun: SunDto,
    @SerializedName("name")
    val cityName: String
)

data class WeatherDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainDto(
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double
)

data class WindDto(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

data class CloudsDto(
    @SerializedName("all")
    val cloudiness: Int
)

data class SunDto(
    val country: String,
    val sunrise: Int,
    val sunset: Int
)

fun WeatherModelDto.toWeatherModel(): WeatherModel {
    return WeatherModel(
        weather = weather.map {
            Weather(
                id = it.id,
                main = it.main,
                description = it.description,
                icon = it.icon
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
        cityName = cityName
    )
}
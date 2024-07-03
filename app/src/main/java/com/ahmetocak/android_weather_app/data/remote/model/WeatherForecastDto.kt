package com.ahmetocak.android_weather_app.data.remote.model

import com.ahmetocak.android_weather_app.model.City
import com.ahmetocak.android_weather_app.model.Clouds
import com.ahmetocak.android_weather_app.model.Main
import com.ahmetocak.android_weather_app.model.Weather
import com.ahmetocak.android_weather_app.model.WeatherForecastModel
import com.ahmetocak.android_weather_app.model.WeatherList
import com.ahmetocak.android_weather_app.model.Wind
import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("list")
    val weather: List<WeatherListDto>,
    val city: CityDto
)

data class CityDto(
    val name: String,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class WeatherListDto(
    val main: MainDto,
    val weather: List<WeatherDto>,
    val clouds: CloudsDto,
    val wind: WindDto,

    @SerializedName("pop")
    val pop: Double,

    @SerializedName("dt")
    val date: Long
)

fun WeatherForecastDto.toWeatherForecastModel(): WeatherForecastModel {
    return WeatherForecastModel(
        weather = weather.map {
            WeatherList(
                main = Main(
                    temp = it.main.temp,
                    feelsLike = it.main.feelsLike,
                    pressure = it.main.pressure,
                    tempMax = it.main.tempMax,
                    tempMin = it.main.tempMin,
                    humidity = it.main.humidity
                ),
                weather = it.weather.map { weatherDto ->
                    Weather(
                        id = weatherDto.id,
                        main = weatherDto.main,
                        description = weatherDto.description
                    )
                },
                clouds = Clouds(cloudiness = it.clouds.cloudiness),
                wind = Wind(
                    speed = it.wind.speed,
                    deg = it.wind.deg,
                    gust = it.wind.gust
                ),
                pop = it.pop,
                date = it.date
            )
        },
        city = City(
            name = city.name,
            country = city.country,
            sunset = city.sunset,
            sunrise = city.sunrise
        )
    )
}
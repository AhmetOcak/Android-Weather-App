package com.ahmetocak.android_weather_app.feature.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ahmetocak.android_weather_app.model.WeatherForecastModel
import com.ahmetocak.android_weather_app.ui.CurrentWeatherInfo
import com.ahmetocak.android_weather_app.ui.ItemDailyForecastModel
import com.ahmetocak.android_weather_app.ui.ItemDailyForecastModelList
import com.ahmetocak.android_weather_app.ui.ItemThreeHourForecastModel
import com.ahmetocak.android_weather_app.util.formatDate
import com.ahmetocak.android_weather_app.util.isDayNight
import com.ahmetocak.android_weather_app.util.readTimestamp
import com.ahmetocak.android_weather_app.util.toRoundedString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherDetailUiState())
    val uiState get() = _uiState.asStateFlow()

    private var threeHourlyForecast: WeatherForecastModel? = null
    private var is24HourFormat by Delegates.notNull<Boolean>()

    init {
        with(savedStateHandle) {
            threeHourlyForecast = get<WeatherForecastModel>("three_hourly_forecast")
            is24HourFormat = get<Boolean>("is_24_hour_format") ?: false
            Log.d("format", is24HourFormat.toString())
            _uiState.update {
                it.copy(
                    currentWeatherInfo = get<CurrentWeatherInfo>("current_weather_info"),
                    dailyForecast = get<ItemDailyForecastModelList>("daily_forecast")?.itemDailyForecastModel
                        ?: emptyList(),
                    weatherDetails = threeHourlyForecast?.let { data ->
                        with(data.weather.first()) {
                            WeatherDetails(
                                windSpeed = wind.speed.toRoundedString(),
                                windDegree = wind.deg.toString(),
                                windGust = wind.gust.toRoundedString(),
                                sunset = data.city.sunset.readTimestamp(is24HourFormat),
                                sunrise = data.city.sunrise.readTimestamp(is24HourFormat),
                                cloudiness = clouds.cloudiness.toString(),
                                humidity = main.humidity.toString(),
                                pressure = main.pressure.toString()
                            )
                        }
                    } ?: run { null },
                    todayThreeHourlyForecast = threeHourlyForecast?.weather?.map { data ->
                        ItemThreeHourForecastModel(
                            temp = "${data.main.temp.toRoundedString()}°",
                            description = data.weather.first().description,
                            mainDescription = data.weather.first().main,
                            weatherDate = data.date.formatDate(is24HourFormat)
                        )
                    } ?: emptyList()
                )
            }
        }
    }

    fun setWeatherData(itemDailyForecastModel: ItemDailyForecastModel) {
        val weatherData = threeHourlyForecast?.weather?.find {
            it.date.formatDate(false).day == itemDailyForecastModel.weatherDate.day
        }
        val city = threeHourlyForecast?.city

        if (weatherData != null && city != null) {
            _uiState.update {
                it.copy(
                    currentWeatherInfo = CurrentWeatherInfo(
                        currentTemp = "${weatherData.main.temp.toRoundedString()}°",
                        feelsLike = weatherData.main.feelsLike.toRoundedString(),
                        mainDescription = weatherData.weather.first().main,
                        description = weatherData.weather.first().description,
                        cityAndCountry = "${city.name}, ${city.country}",
                        maxTemp = weatherData.main.tempMax.toRoundedString(),
                        minTemp = weatherData.main.tempMin.toRoundedString(),
                        isNight = weatherData.date.isDayNight()
                    ),
                    weatherDetails = WeatherDetails(
                        windSpeed = weatherData.wind.speed.toRoundedString(),
                        windDegree = weatherData.wind.deg.toString(),
                        windGust = weatherData.wind.gust.toRoundedString(),
                        humidity = weatherData.main.humidity.toString(),
                        pressure = weatherData.main.pressure.toString(),
                        cloudiness = weatherData.clouds.cloudiness.toString(),
                        sunrise = city.sunrise.readTimestamp(is24HourFormat),
                        sunset = city.sunset.readTimestamp(is24HourFormat)
                    )
                )
            }
        }
    }
}

data class WeatherDetailUiState(
    val currentWeatherInfo: CurrentWeatherInfo? = null,
    val weatherDetails: WeatherDetails? = null,
    val dailyForecast: List<ItemDailyForecastModel> = emptyList(),
    val todayThreeHourlyForecast: List<ItemThreeHourForecastModel> = emptyList()
)
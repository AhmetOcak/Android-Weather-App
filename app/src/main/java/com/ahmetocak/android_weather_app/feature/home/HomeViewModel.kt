package com.ahmetocak.android_weather_app.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.android_weather_app.data.WeatherRepository
import com.ahmetocak.android_weather_app.data.di.AppDispatchers
import com.ahmetocak.android_weather_app.data.di.Dispatcher
import com.ahmetocak.android_weather_app.data.local.LocationPreferenceManager
import com.ahmetocak.android_weather_app.model.BaseResponse
import com.ahmetocak.android_weather_app.model.WeatherForecastModel
import com.ahmetocak.android_weather_app.model.WeatherList
import com.ahmetocak.android_weather_app.ui.CurrentWeatherInfo
import com.ahmetocak.android_weather_app.ui.ItemDailyForecastModel
import com.ahmetocak.android_weather_app.ui.ItemThreeHourForecastModel
import com.ahmetocak.android_weather_app.util.formatDate
import com.ahmetocak.android_weather_app.util.isDayNight
import com.ahmetocak.android_weather_app.util.toErrorMessage
import com.ahmetocak.android_weather_app.util.toRoundedString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val locationPreferenceManager: LocationPreferenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState get() = _uiState.asStateFlow()

    var threeHourlyForecastData: WeatherForecastModel? = null
        private set

    init {
        _uiState.update {
            it.copy(uiEvents = listOf(HomeScreenUiEvent.Init))
        }
    }

    fun getCurrentWeatherData(lat: Double, long: Double) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = weatherRepository.getCurrentWeatherData(lat, long)) {
                is BaseResponse.Success -> {
                    with(response.data) {
                        _uiState.update {
                            it.copy(
                                dataStatus = it.dataStatus.copy(currentWeatherDataStatus = Status.END),
                                currentWeatherInfo = CurrentWeatherInfo(
                                    currentTemp = "${main.temp.toRoundedString()}°",
                                    feelsLike = main.feelsLike.toRoundedString(),
                                    mainDescription = weather.first().main,
                                    description = weather.first().description,
                                    cityAndCountry = "$cityName, ${sun.country}",
                                    maxTemp = main.tempMax.toRoundedString(),
                                    minTemp = main.tempMin.toRoundedString(),
                                    isNight = date.isDayNight()
                                )
                            )
                        }
                    }
                }

                is BaseResponse.Error -> {
                    Log.e("getCurrentWeatherData", response.exception.stackTraceToString())
                    _uiState.update {
                        it.copy(
                            dataStatus = it.dataStatus.copy(
                                currentWeatherDataStatus = Status.END
                            ),
                            errorMessage = listOf(response.exception.toErrorMessage())
                        )
                    }
                }
            }
        }
    }

    fun getThreeHourlyForecast(lat: Double, long: Double, is24HourFormat: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = weatherRepository.getWeatherForecastData(lat, long)) {
                is BaseResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            dataStatus = it.dataStatus.copy(
                                weatherForecastDataStatus = Status.END
                            ),
                            todayThreeHourlyForecast = response.data.weather.map { data ->
                                ItemThreeHourForecastModel(
                                    temp = "${data.main.temp.toRoundedString()}°",
                                    description = data.weather.first().description,
                                    mainDescription = data.weather.first().main,
                                    weatherDate = data.date.formatDate(is24HourFormat)
                                )
                            },
                            dailyForecast = calculateAverageDailyTemp(
                                weather = response.data.weather,
                                is24HourFormat = is24HourFormat
                            )
                        )
                    }
                    threeHourlyForecastData = response.data
                }

                is BaseResponse.Error -> {
                    _uiState.update {
                        it.copy(
                            dataStatus = it.dataStatus.copy(
                                weatherForecastDataStatus = Status.END
                            ),
                            errorMessage = listOf(response.exception.toErrorMessage())
                        )
                    }
                }
            }
        }
    }

    private fun calculateAverageDailyTemp(
        weather: List<WeatherList>,
        is24HourFormat: Boolean
    ): List<ItemDailyForecastModel> {
        val dailyForecastList = mutableListOf<ItemDailyForecastModel>()

        for (i in weather.indices step 4) {
            var minTemp = 0.0
            var maxTemp = 0.0

            for (j in i until i + 4) {
                minTemp += weather[j].main.tempMin
                maxTemp += weather[j].main.tempMax
            }

            dailyForecastList.add(
                ItemDailyForecastModel(
                    weatherDate = weather[i].date
                        .formatDate(is24HourFormat)
                        .copy(isDayNight = false),
                    description = weather[i].weather.first().description,
                    mainDescription = weather[i].weather.first().main,
                    minTemp = (minTemp / 4).toRoundedString(),
                    maxTemp = (maxTemp / 4).toRoundedString()
                )
            )
        }

        return dailyForecastList.distinctBy { it.weatherDate.day }
    }

    fun getLocationFromCache(is24HourFormat: Boolean, onCacheNull: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            val location = async { locationPreferenceManager.getLocation() }.await()
            if (location != null) {
                with(location) {
                    getCurrentWeatherData(latitude, longitude)
                    getThreeHourlyForecast(latitude, longitude, is24HourFormat)
                }
                Log.d("getLocationFromCache", "location getting from cache")
            } else onCacheNull()
        }
    }

    fun cacheLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch(ioDispatcher) {
            locationPreferenceManager.saveLocation(latitude, longitude)
        }
    }

    fun consumedErrorMessage() {
        _uiState.update { it.copy(errorMessage = emptyList()) }
    }
}

data class HomeUiState(
    val dataStatus: DataStatus = DataStatus(
        currentWeatherDataStatus = Status.LOADING,
        weatherForecastDataStatus = Status.LOADING
    ),
    val currentWeatherInfo: CurrentWeatherInfo? = null,
    val todayThreeHourlyForecast: List<ItemThreeHourForecastModel> = emptyList(),
    val dailyForecast: List<ItemDailyForecastModel> = emptyList(),
    val errorMessage: List<String> = emptyList(),
    val uiEvents: List<HomeScreenUiEvent> = emptyList()
)

data class DataStatus(
    val currentWeatherDataStatus: Status,
    val weatherForecastDataStatus: Status
)

enum class Status {
    LOADING,
    END
}

sealed interface HomeScreenUiEvent {
    data object Init: HomeScreenUiEvent
}
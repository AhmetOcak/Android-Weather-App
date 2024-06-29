package com.ahmetocak.android_weather_app.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.android_weather_app.data.WeatherRepository
import com.ahmetocak.android_weather_app.data.di.AppDispatchers
import com.ahmetocak.android_weather_app.data.di.Dispatcher
import com.ahmetocak.android_weather_app.model.BaseResponse
import com.ahmetocak.android_weather_app.ui.ItemThreeHourForecastModel
import com.ahmetocak.android_weather_app.util.formatDate
import com.ahmetocak.android_weather_app.util.isDayNight
import com.ahmetocak.android_weather_app.util.toErrorMessage
import com.ahmetocak.android_weather_app.util.toRoundedString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState get() = _uiState.asStateFlow()

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
                            }
                        )
                    }
                }

                is BaseResponse.Error -> {}
            }
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
    val errorMessage: List<String> = emptyList()
)

data class DataStatus(
    val currentWeatherDataStatus: Status,
    val weatherForecastDataStatus: Status
)

enum class Status {
    LOADING,
    END
}
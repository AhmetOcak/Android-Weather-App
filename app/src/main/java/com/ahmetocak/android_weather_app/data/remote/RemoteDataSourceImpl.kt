package com.ahmetocak.android_weather_app.data.remote

import com.ahmetocak.android_weather_app.data.di.AppDispatchers
import com.ahmetocak.android_weather_app.data.di.Dispatcher
import com.ahmetocak.android_weather_app.data.remote.api.OpenWeatherApi
import com.ahmetocak.android_weather_app.data.remote.model.WeatherModelDto
import com.ahmetocak.android_weather_app.model.BaseResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val api: OpenWeatherApi,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : RemoteDataSource {

    override suspend fun getCurrentWeatherData(
        latitude: Double,
        longitude: Double
    ): BaseResponse<WeatherModelDto> {
        return remoteResponseWrapper { api.getCurrentWeather(latitude, longitude) }
    }

    private suspend fun <T> remoteResponseWrapper(
        apiCall: suspend () -> Response<T>
    ): BaseResponse<T> {
        return withContext(ioDispatcher) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    response.body()?.let {
                        BaseResponse.Success(it)
                    } ?: run { BaseResponse.Error(Exception("body null")) }
                } else {
                    BaseResponse.Error(HttpException(response))
                }
            } catch (e: Exception) {
                BaseResponse.Error(e)
            }
        }
    }
}
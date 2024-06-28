package com.ahmetocak.android_weather_app.model

import java.lang.Exception

sealed class BaseResponse<out T> {
    data class Success<T>(val data: T) : BaseResponse<T>()
    data class Error(val exception: Exception) : BaseResponse<Nothing>()
}
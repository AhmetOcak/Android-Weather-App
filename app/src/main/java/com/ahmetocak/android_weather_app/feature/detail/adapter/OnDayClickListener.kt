package com.ahmetocak.android_weather_app.feature.detail.adapter

import com.ahmetocak.android_weather_app.ui.ItemDailyForecastModel

interface OnDayClickListener {
    fun onDayClick(itemDailyForecastModel: ItemDailyForecastModel)
}
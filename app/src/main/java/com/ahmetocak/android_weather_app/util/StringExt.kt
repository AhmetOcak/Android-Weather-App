package com.ahmetocak.android_weather_app.util

import com.ahmetocak.android_weather_app.ui.WeatherDate
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

fun Double.toRoundedString(): String {
    return this.roundToInt().toString()
}

fun Exception.toErrorMessage(): String {
    return this.message ?: "Something went wrong. Please try again later!!"
}

fun Long.readTimestamp(is24HourFormat: Boolean): String {
    val instant = Instant.ofEpochSecond(this)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = if (is24HourFormat) {
        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    } else DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
    return formatter.format(localDateTime)
}

fun Long.isDayNight(): Boolean {
    val instant = Instant.ofEpochSecond(this)
    val localTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime()
    val hour = localTime.format(DateTimeFormatter.ofPattern("HH", Locale.getDefault())).toInt()
    return hour >= 21 || hour <= 6
}

fun Long.formatDate(is24HourFormat: Boolean): WeatherDate {
    val currentTime = Instant.ofEpochSecond(Instant.now().epochSecond)
    val weatherTime = Instant.ofEpochSecond(this)

    val currentDate = LocalDateTime.ofInstant(currentTime, ZoneId.systemDefault()).toLocalDate()
    val weatherDate = LocalDateTime.ofInstant(weatherTime, ZoneId.systemDefault()).toLocalDate()

    return if (currentDate.isEqual(weatherDate)) {
        WeatherDate(
            hour = this.readTimestamp(is24HourFormat),
            day = "Today",
            isDayNight = this.isDayNight()
        )
    } else {
        WeatherDate(
            hour = this.readTimestamp(is24HourFormat),
            day = weatherDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            isDayNight = this.isDayNight()
        )
    }
}

fun Long.toDay(): String {
    val currentTime = Instant.ofEpochSecond(Instant.now().epochSecond)
    val weatherTime = Instant.ofEpochSecond(this)

    val currentDate = LocalDateTime.ofInstant(currentTime, ZoneId.systemDefault()).toLocalDate()
    val weatherDate = LocalDateTime.ofInstant(weatherTime, ZoneId.systemDefault()).toLocalDate()

    return if (currentDate.isEqual(weatherDate)) {
        "Today"
    } else weatherDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
}
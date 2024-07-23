package com.github.mukiva.feature.forecast.domain

import com.github.mukiva.openweather.core.domain.weather.Distance
import com.github.mukiva.openweather.core.domain.weather.Precipitation
import com.github.mukiva.openweather.core.domain.weather.Pressure
import com.github.mukiva.openweather.core.domain.weather.Speed
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.openweather.core.domain.weather.WindDirection
import kotlinx.datetime.LocalDateTime

internal data class ForecastItem(
    val id: Int,
    val humidity: Int,
    val pressure: Pressure,
    val precipitation: Precipitation,
    val temp: Temp,
    val feelsLike: Temp,
    val cloud: Int,
    val weatherIconCode: Int,
    val isDay: Boolean,
    val dateTime: LocalDateTime,
    val windSpeed: Speed,
    val windDegree: Int,
    val windDirection: WindDirection,
    val vis: Distance,
    val gust: Speed,
)

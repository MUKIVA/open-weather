package com.mukiva.feature.forecast.domain

import com.github.mukiva.open_weather.core.domain.Distance
import com.github.mukiva.open_weather.core.domain.Precipitation
import com.github.mukiva.open_weather.core.domain.Pressure
import com.github.mukiva.open_weather.core.domain.Speed
import com.github.mukiva.open_weather.core.domain.Temp
import com.github.mukiva.open_weather.core.domain.WindDirection
import kotlinx.datetime.LocalDateTime

data class ForecastItem(
    val id: Int,
    val humidity: Int,
    val pressure: Pressure,
    val precipitation: Precipitation,
    val temp: Temp,
    val feelsLike: Temp,
    val cloud: Int,
    val weatherIconUrl: String,
    val dateTime: LocalDateTime,
    val windSpeed: Speed,
    val windDegree: Int,
    val windDirection: WindDirection,
    val vis: Distance,
    val gust: Speed,
)
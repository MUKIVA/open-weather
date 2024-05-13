package com.mukiva.feature.forecast.domain

import com.github.mukiva.open_weather.core.domain.weather.Distance
import com.github.mukiva.open_weather.core.domain.weather.Precipitation
import com.github.mukiva.open_weather.core.domain.weather.Pressure
import com.github.mukiva.open_weather.core.domain.weather.Speed
import com.github.mukiva.open_weather.core.domain.weather.Temp
import com.github.mukiva.open_weather.core.domain.weather.WindDirection
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

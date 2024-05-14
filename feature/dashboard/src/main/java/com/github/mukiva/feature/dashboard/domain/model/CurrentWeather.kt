package com.github.mukiva.feature.dashboard.domain.model

import com.github.mukiva.openweather.core.domain.weather.Distance
import com.github.mukiva.openweather.core.domain.weather.Precipitation
import com.github.mukiva.openweather.core.domain.weather.Pressure
import com.github.mukiva.openweather.core.domain.weather.Speed
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.openweather.core.domain.weather.WindDirection
import kotlinx.datetime.LocalDateTime

data class CurrentWeather(
    val lastUpdatedEpoch: LocalDateTime,
    val temp: Temp,
    val isDay: Boolean,
    val condition: Condition,
    val windSpeed: Speed,
    val windDegree: Int,
    val windDir: WindDirection,
    val pressure: Pressure,
    val precip: Precipitation,
    val humidity: Int,
    val cloud: Int,
    val feelsLike: Temp,
    val vis: Distance,
    val uv: Double,
    val gust: Speed,
)

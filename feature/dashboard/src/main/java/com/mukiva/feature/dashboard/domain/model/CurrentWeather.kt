package com.mukiva.feature.dashboard.domain.model

import com.github.mukiva.open_weather.core.domain.Distance
import com.github.mukiva.open_weather.core.domain.Precipitation
import com.github.mukiva.open_weather.core.domain.Pressure
import com.github.mukiva.open_weather.core.domain.Speed
import com.github.mukiva.open_weather.core.domain.Temp
import com.github.mukiva.open_weather.core.domain.WindDirection
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
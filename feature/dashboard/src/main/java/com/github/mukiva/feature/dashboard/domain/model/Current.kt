package com.github.mukiva.feature.dashboard.domain.model

import com.github.mukiva.openweather.core.domain.weather.Pressure
import com.github.mukiva.openweather.core.domain.weather.Speed
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.openweather.core.domain.weather.WindDirection

data class Current(
    val isDay: Boolean,
    val temp: Temp,
    val feelsLike: Temp,
    val cloudPercent: Int,
    val windSpeed: Speed,
    val windDirection: WindDirection,
    val humidityPercent: Int,
    val pressure: Pressure,
    val conditionImageCode: Int,
)
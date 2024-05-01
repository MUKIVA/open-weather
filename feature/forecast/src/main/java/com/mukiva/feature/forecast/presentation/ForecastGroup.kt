package com.mukiva.feature.forecast.presentation

import com.mukiva.feature.forecast.domain.ForecastItem

data class ForecastGroup(
    val forecastType: Type,
    val forecast: List<ForecastItem>,
) {
    enum class Type {
        TEMP,
        WIND,
        PRESSURE,
        HUMIDITY,
    }
}
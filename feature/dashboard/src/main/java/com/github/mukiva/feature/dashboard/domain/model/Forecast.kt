package com.github.mukiva.feature.dashboard.domain.model

internal data class Forecast(
    val location: Location,
    val current: Current,
    val precipitation: Precipitation,
    val astro: Astro,
    val dayForecasts: List<DayForecast>
)
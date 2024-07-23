package com.github.mukiva.feature.dashboard.domain.model

data class Forecast(
    val location: Location,
    val current: Current,
    val precipitation: Precipitation,
    val astro: Astro,
    val dayForecasts: List<DayForecast>
)
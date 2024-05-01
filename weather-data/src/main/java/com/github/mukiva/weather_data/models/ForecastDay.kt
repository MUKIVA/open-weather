package com.github.mukiva.weather_data.models

import kotlinx.datetime.LocalDateTime

data class ForecastDay(
    val dateEpoch: LocalDateTime,
    val day: Day,
    val astro: Astro,
    val hour: List<Hour>,
)
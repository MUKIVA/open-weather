package com.github.mukiva.weatherdata.models

import kotlinx.datetime.LocalDateTime

public data class ForecastDay(
    val dateEpoch: LocalDateTime,
    val day: Day,
    val astro: Astro,
    val hour: List<Hour>,
)
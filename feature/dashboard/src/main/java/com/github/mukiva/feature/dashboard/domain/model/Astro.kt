package com.github.mukiva.feature.dashboard.domain.model

import kotlinx.datetime.LocalTime

data class Astro(
    val sunrise: LocalTime,
    val sunset: LocalTime,
    val moonrise: LocalTime,
    val moonset: LocalTime
)
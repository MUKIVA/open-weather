package com.mukiva.feature.dashboard_impl.domain.model

import java.time.LocalDateTime

data class Astro(
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime,
    val moonrise: LocalDateTime,
    val moonSet: LocalDateTime,
    val moonPhase: MoonPhase,
    val moonIllumination: Int,
    val isMoonUp: Int,
    val isSunUp: Int
)
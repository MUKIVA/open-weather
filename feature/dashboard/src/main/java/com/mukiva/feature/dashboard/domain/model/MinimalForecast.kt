package com.mukiva.feature.dashboard.domain.model

import kotlinx.datetime.LocalDateTime

data class MinimalForecast(
    val id: Int,
    val avgTempC: Double,
    val avgTempF: Double,
    val minTempC: Double,
    val minTempF: Double,
    val maxTempC: Double,
    val maxTempF: Double,
    val conditionIconUrl: String,
    val date: LocalDateTime,
)
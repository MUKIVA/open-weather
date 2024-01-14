package com.mukiva.feature.forecast_impl.domain

import java.util.Date

data class Forecast(
    val id: Int,
    val dayAvgTempC: Double,
    val dayAvgTempF: Double,
    val nightAvgTempC: Double,
    val nightAvgTempF: Double,
    val date: Date
)
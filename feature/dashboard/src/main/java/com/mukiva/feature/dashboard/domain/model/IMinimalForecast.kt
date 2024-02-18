package com.mukiva.feature.dashboard.domain.model

import java.util.Date

interface IMinimalForecast {
    val index: Int
    val dayAvgTempC: Double
    val dayAvgTempF: Double
    val nightAvgTempC: Double
    val nightAvgTempF: Double
    val conditionIconUrl: String
    val date: Date
}
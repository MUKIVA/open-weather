package com.github.mukiva.weatherdata.models

import kotlinx.datetime.LocalDateTime

public data class ForecastDayData(
    val dateEpoch: LocalDateTime,
    val dayData: DayData,
    val astroData: AstroData,
    val hourData: List<HourData>,
)
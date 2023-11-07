package com.mukiva.openweather.domain

import java.time.LocalDateTime

data class Forecast(
    val forecastDayList: List<ForecastDay>
)

data class ForecastDay(
    val date: LocalDateTime,
    val dateEpoch: Int,
    val day: Day
)

data class Day(
    val maxTempC: Float,
    val maxTempF: Float,
    val minTempC: Float,
    val minTempF: Float,
    val avgTempC: Float,
    val avgTempF: Float,
    val maxWindMph: Float,
    val maxWindKph: Float,
    val totalPrecipMm: Float,
    val totalPrecipIn: Float,
    val totalSnowCm: Float,
    val avgVisKm: Float,
    val avgVisMiles: Float,
    val avgHumidity: Float,
    val dailyWillItRain: Int,
    val dailyChanceOfRain: Int,
    val dailyWillItSnow: Int,
    val dailyChanceOfSnow: Int,
    val condition: Condition,
    val uv: Float
)
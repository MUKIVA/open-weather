package com.mukiva.feature.forecast.domain

import java.util.Date

sealed class ForecastItem(
    val id: Int
) {
    data class HourlyHumidity(
        val humidity: Int,
        val date: Date,
    ) : ForecastItem(HUMIDITY_ID)
    data class HourlyPressure(
        val pressureMb: Double,
        val pressureIn: Double,
        val date: Date,
    ) : ForecastItem(PRESSURE_ID)
    data class HourlyTemp(
        val tempC: Double,
        val tempF: Double,
        val feelsLikeC: Double,
        val feelsLikeF: Double,
        val cloud: Int,
        val iconUrl: String,
        val date: Date,
    ) : ForecastItem(TEMP_ID)

    data class HourlyWind(
        val windMph: Double,
        val windKph: Double,
        val windDegree: Int,
        val windDirection: WindDirection,
        val date: Date,
    ) : ForecastItem(WIND_ID)

    companion object {
        private const val HUMIDITY_ID = 0
        private const val PRESSURE_ID = 1
        private const val TEMP_ID = 2
        private const val WIND_ID = 3

    }
}
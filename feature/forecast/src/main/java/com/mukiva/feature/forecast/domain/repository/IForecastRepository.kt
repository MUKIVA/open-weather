package com.mukiva.feature.forecast.domain.repository

import com.mukiva.feature.forecast.presentation.HourlyForecast

interface IForecastRepository {
    suspend fun getFullForecast(locationName: String, days: Int): List<HourlyForecast>

}
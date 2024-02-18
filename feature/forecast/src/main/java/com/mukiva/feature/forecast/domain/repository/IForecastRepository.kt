package com.mukiva.feature.forecast.domain.repository

import com.mukiva.feature.forecast.domain.IHourlyForecast

interface IForecastRepository {
    suspend fun getFullForecast(locationName: String, days: Int): List<IHourlyForecast>

}
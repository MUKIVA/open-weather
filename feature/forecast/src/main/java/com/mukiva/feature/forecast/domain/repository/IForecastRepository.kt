package com.mukiva.feature.forecast.domain.repository

import com.mukiva.feature.forecast.domain.IMinimalForecast
import com.mukiva.feature.forecast.domain.IHourlyForecast

interface IForecastRepository {

    suspend fun getMinimalForecast(locationName: String, days: Int): List<IMinimalForecast>

    suspend fun getFullForecast(locationName: String, days: Int): List<IHourlyForecast>

}
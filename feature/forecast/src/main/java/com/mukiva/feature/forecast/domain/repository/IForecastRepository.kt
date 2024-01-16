package com.mukiva.feature.forecast.domain.repository

import com.mukiva.feature.forecast.domain.Forecast

interface IForecastRepository {

    suspend fun getForecast(locationName: String, days: Int): List<Forecast>

}
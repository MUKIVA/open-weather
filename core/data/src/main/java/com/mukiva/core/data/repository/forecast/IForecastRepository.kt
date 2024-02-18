package com.mukiva.core.data.repository.forecast

import com.mukiva.core.data.repository.forecast.entity.ForecastWithCurrentAndLocationRemote

interface IForecastRepository {
    suspend fun getCurrent(locationName: String): ForecastWithCurrentAndLocationRemote
    suspend fun getForecast(
        locationName: String,
        days: Int,
        aqi: Boolean = false,
        alerts: Boolean = false
    ): ForecastWithCurrentAndLocationRemote

}
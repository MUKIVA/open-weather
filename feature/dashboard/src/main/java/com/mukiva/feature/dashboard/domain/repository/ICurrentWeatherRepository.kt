package com.mukiva.feature.dashboard.domain.repository

import com.mukiva.feature.dashboard.domain.model.Forecast

interface ICurrentWeatherRepository {

    suspend fun getCurrent(locationName: String): Forecast

}
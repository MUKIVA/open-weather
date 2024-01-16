package com.mukiva.feature.dashboard.domain.repository

import com.mukiva.feature.dashboard.domain.model.CurrentWithLocation

interface ICurrentWeatherRepository {

    suspend fun getCurrent(locationName: String): CurrentWithLocation

}
package com.github.mukiva.feature.dashboard.presentation

import kotlinx.coroutines.flow.Flow

interface IForecastLoader {
    fun loadForecast(locationId: Long)
    suspend fun provideForecastState(locationId: Long): Flow<ICurrentState>
    fun cleanLoadedData()
}
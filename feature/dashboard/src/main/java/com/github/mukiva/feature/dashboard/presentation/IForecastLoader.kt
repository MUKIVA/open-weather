package com.github.mukiva.feature.dashboard.presentation

import kotlinx.coroutines.flow.Flow

internal interface IForecastLoader {
    fun loadForecast(locationId: Long)
    fun provideForecastState(locationId: Long): Flow<ICurrentState>
    fun cleanLoadedData()
}
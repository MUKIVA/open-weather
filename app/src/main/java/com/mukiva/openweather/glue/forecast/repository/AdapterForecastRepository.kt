package com.mukiva.openweather.glue.forecast.repository

import com.mukiva.core.data.repository.forecast.entity.ForecastRemote
import com.mukiva.feature.forecast.presentation.HourlyForecast
import com.mukiva.feature.forecast.domain.repository.IForecastRepository
import com.mukiva.openweather.glue.forecast.FullForecastMapper
import com.mukiva.core.data.repository.forecast.IForecastRepository as ICoreForecastRepository
import javax.inject.Inject

class AdapterForecastRepository @Inject constructor(
    private val coreRepository: ICoreForecastRepository,
    private val fullForecastMapper: FullForecastMapper
) : IForecastRepository {

    override suspend fun getFullForecast(locationName: String, days: Int): List<HourlyForecast> {
        val forecast = coreRepository
            .getForecast(locationName, days)
            .forecast
        return fullForecastMapper.asDomain(forecast ?: ForecastRemote())
    }

}
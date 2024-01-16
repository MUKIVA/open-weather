package com.mukiva.openweather.glue.forecast.repository

import com.mukiva.core.data.repository.forecast.entity.ForecastRemote
import com.mukiva.feature.forecast.domain.Forecast
import com.mukiva.feature.forecast.domain.repository.IForecastRepository
import com.mukiva.openweather.glue.forecast.ForecastMapper
import com.mukiva.core.data.repository.forecast.IForecastRepository as ICoreForecastRepository
import javax.inject.Inject

class AdapterForecastRepository @Inject constructor(
    private val coreRepository: ICoreForecastRepository,
    private val forecastMapper: ForecastMapper
) : IForecastRepository {
    override suspend fun getForecast(locationName: String, days: Int): List<Forecast> {
        val forecast = coreRepository
            .getForecast(locationName, days)
            .forecast
        return forecastMapper.asDomain(forecast ?: ForecastRemote())
    }

}
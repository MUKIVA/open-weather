package com.mukiva.openweather.glue.forecast.repository

import com.mukiva.core.data.repository.forecast.entity.ForecastRemote
import com.mukiva.feature.forecast.domain.IHourlyForecast
import com.mukiva.feature.forecast.domain.IMinimalForecast
import com.mukiva.feature.forecast.domain.repository.IForecastRepository
import com.mukiva.openweather.glue.forecast.ForecastMapper
import com.mukiva.openweather.glue.forecast.FullForecastMapper
import com.mukiva.core.data.repository.forecast.IForecastRepository as ICoreForecastRepository
import javax.inject.Inject

class AdapterForecastRepository @Inject constructor(
    private val coreRepository: ICoreForecastRepository,
    private val forecastMapper: ForecastMapper,
    private val fullForecastMapper: FullForecastMapper
) : IForecastRepository {
    override suspend fun getMinimalForecast(locationName: String, days: Int): List<IMinimalForecast> {
        val forecast = coreRepository
            .getForecast(locationName, days)
            .forecast
        return forecastMapper.asDomain(forecast ?: ForecastRemote())
    }

    override suspend fun getFullForecast(locationName: String, days: Int): List<IHourlyForecast> {
        val forecast = coreRepository
            .getForecast(locationName, days)
            .forecast
        return fullForecastMapper.asDomain(forecast ?: ForecastRemote())
    }

}
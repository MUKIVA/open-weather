package com.mukiva.openweather.glue.dashboard.repository

import com.mukiva.core.data.repository.forecast.IForecastRepository
import com.mukiva.feature.dashboard.domain.model.CurrentWithLocation
import com.mukiva.feature.dashboard.domain.repository.ICurrentWeatherRepository
import com.mukiva.openweather.glue.dashboard.mapper.CurrentWithLocationMapper
import javax.inject.Inject

class AdapterCurrentWeatherRepository @Inject constructor(
    private val coreRepository: IForecastRepository,
    private val currentWithLocationMapper: CurrentWithLocationMapper
) : ICurrentWeatherRepository {
    override suspend fun getCurrent(locationName: String): CurrentWithLocation {
        return currentWithLocationMapper.mapToDomain(
            coreRepository.getCurrent(locationName)
        )
    }
}
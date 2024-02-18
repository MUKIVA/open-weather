package com.mukiva.openweather.glue.dashboard.repository

import com.mukiva.feature.dashboard.domain.model.ILocation
import com.mukiva.feature.dashboard.domain.repository.ILocationRepository
import com.mukiva.openweather.glue.dashboard.mapper.LocationMapper
import com.mukiva.core.data.repository.location.ILocationRepository as ICoreLocationRepository
import javax.inject.Inject

class AdapterLocationRepository @Inject constructor(
    private val coreRepository: ICoreLocationRepository
) : ILocationRepository {

    override suspend fun getAllLocations(): List<ILocation> {
        return coreRepository.getAllLocal().map {
            LocationMapper.mapToDomain(it)
        }
    }
}
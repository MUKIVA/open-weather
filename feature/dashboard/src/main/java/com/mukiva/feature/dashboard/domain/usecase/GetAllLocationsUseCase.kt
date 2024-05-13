package com.mukiva.feature.dashboard.domain.usecase

import com.github.mukiva.weather_data.LocationRepository
import com.github.mukiva.weather_data.utils.RequestResult
import com.github.mukiva.weather_data.utils.map
import com.mukiva.feature.dashboard.domain.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.github.mukiva.weather_data.models.Location as DataLocation

class GetAllLocationsUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
) {
    operator fun invoke(): Flow<RequestResult<List<Location>>> {
        return locationRepository.getAllLocal()
            .map { requestResult -> requestResult.map(::toLocation) }
    }

    private fun toLocation(dataList: List<DataLocation>): List<Location> {
        return dataList.map(::toLocation)
    }

    private fun toLocation(data: DataLocation): Location {
        return Location(
            id = data.id,
            name = data.name,
            region = data.region,
            country = data.country,
            position = data.priority,
        )
    }
}

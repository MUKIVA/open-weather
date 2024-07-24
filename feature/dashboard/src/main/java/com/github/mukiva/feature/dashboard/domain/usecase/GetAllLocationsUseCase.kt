package com.github.mukiva.feature.dashboard.domain.usecase

import com.github.mukiva.feature.dashboard.domain.model.Location
import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.github.mukiva.weatherdata.models.LocationData as DataLocation

internal class GetAllLocationsUseCase @Inject constructor(
    private val locationRepository: ILocationRepository,
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
            priority = data.priority,
        )
    }
}

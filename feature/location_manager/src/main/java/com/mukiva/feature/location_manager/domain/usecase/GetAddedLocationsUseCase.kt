package com.mukiva.feature.location_manager.domain.usecase

import com.github.mukiva.weather_data.LocationRepository
import com.github.mukiva.weather_data.utils.RequestResult
import com.github.mukiva.weather_data.utils.map
import com.mukiva.feature.location_manager.domain.model.Location
import com.mukiva.feature.location_manager.presentation.EditableLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.github.mukiva.weather_data.models.Location as DataLocation

class GetAddedLocationsUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    operator fun invoke(): Flow<RequestResult<List<EditableLocation>>> {
        return repository.getAllLocal()
            .map { requestResult ->
                requestResult.map { dataLocations ->
                    dataLocations.map(::toEditable)
                }
            }
    }

    private fun toEditable(location: DataLocation) = EditableLocation(
        location = toLocation(location),
        isSelected = false,
        isEditable = false
    )

    private fun toLocation(dataLocation: DataLocation): Location {
        return Location(
            id = dataLocation.id.toInt(),
            position = dataLocation.priority,
            cityName = dataLocation.name,
            regionName = dataLocation.region,
            countryName = dataLocation.country,
            isAdded = true
        )
    }
}

package com.github.mukiva.feature.locationmanager.domain.usecase

import com.github.mukiva.feature.locationmanager.domain.model.Location
import com.github.mukiva.feature.locationmanager.presentation.EditableLocation
import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.github.mukiva.weatherdata.models.Location as DataLocation

class GetAddedLocationsUseCase @Inject constructor(
    private val repository: ILocationRepository
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

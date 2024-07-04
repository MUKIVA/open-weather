package com.github.mukiva.feature.locationmanager.domain.usecase

import com.github.mukiva.feature.locationmanager.presentation.EditableLocation
import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import com.github.mukiva.weatherdata.models.Location as DataLocation

class UpdateStoredLocationsUseCase @Inject constructor(
    private val repository: ILocationRepository
) {

    suspend operator fun invoke(newList: List<EditableLocation>): RequestResult<Unit> {
        val dataLocation = newList.mapIndexed { index, editableLocation ->
            editableLocation.toDataLocation(index)
        }
        return repository.updateLocations(dataLocation)
    }

    private fun EditableLocation.toDataLocation(
        priority: Int
    ): DataLocation {
        return DataLocation(
            id = location.id.toLong(),
            name = location.cityName,
            region = location.regionName,
            country = location.countryName,
            lat = 0.0,
            lon = 0.0,
            tzId = "",
            localtimeEpoch = Clock.System.now().toLocalDateTime(TimeZone.UTC),
            priority = priority,
        )
    }
}

package com.mukiva.feature.location_manager.domain.usecase

import com.github.mukiva.weather_data.LocationRepository
import com.github.mukiva.weather_data.utils.RequestResult
import com.mukiva.feature.location_manager.presentation.EditableLocation
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import com.github.mukiva.weather_data.models.Location as DataLocation

class UpdateStoredLocationsUseCase @Inject constructor(
    private val repository: LocationRepository
) {

    suspend operator fun invoke(newList: List<EditableLocation>): RequestResult<Unit> {
        repository.removeAllLocations()
        newList.onEachIndexed { index, editableLocation ->
            repository.addLocalLocation(editableLocation.toDataLocation(index))
        }
        return RequestResult.Success(Unit)
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
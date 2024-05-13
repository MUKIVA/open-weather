package com.mukiva.feature.location_manager.domain.usecase

import com.github.mukiva.weather_data.LocationRepository
import com.github.mukiva.weather_data.utils.RequestResult
import com.mukiva.feature.location_manager.domain.model.Location
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import com.github.mukiva.weather_data.models.Location as DataLocation

class AddLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(location: Location): RequestResult<Unit> {
        repository.addLocalLocation(
            location = location.toData()
        )
        return RequestResult.Success(Unit)
    }

    private fun Location.toData(): DataLocation {
        return DataLocation(
            id = id.toLong(),
            name = cityName,
            region = regionName,
            country = countryName,
            lat = 0.0,
            lon = 0.0,
            tzId = "",
            localtimeEpoch = Clock.System.now().toLocalDateTime(TimeZone.UTC),
            priority = 0,
        )
    }
}


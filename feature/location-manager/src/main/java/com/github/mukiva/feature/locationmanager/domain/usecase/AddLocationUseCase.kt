package com.github.mukiva.feature.locationmanager.domain.usecase

import com.github.mukiva.feature.locationmanager.domain.model.Location
import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import com.github.mukiva.weatherdata.models.LocationData as DataLocation

internal class AddLocationUseCase @Inject constructor(
    private val repository: ILocationRepository
) {
    suspend operator fun invoke(location: Location): RequestResult<Unit> {
        return repository.addLocalLocation(
            locationData = location.toData()
        )
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


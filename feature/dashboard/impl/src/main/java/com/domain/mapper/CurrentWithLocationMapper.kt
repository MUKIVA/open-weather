package com.domain.mapper

import com.data.CurrentWeatherRemote
import com.domain.model.CurrentWithLocation
import javax.inject.Inject

class CurrentWithLocationMapper @Inject constructor(
    private val currentWeatherMapper: CurrentWeatherMapper,
    private val locationMapper: LocationMapper
) {

    fun CurrentWeatherRemote.mapToDomain(): CurrentWithLocation {
        return CurrentWithLocation(
            currentWeather = with(currentWeatherMapper) {
                currentWeather.mapToDomain()
            },
            location = with(locationMapper) {
                location.mapToDomain()
            }
        )
    }

}
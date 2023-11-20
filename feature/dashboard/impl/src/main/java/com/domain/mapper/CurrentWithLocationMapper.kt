package com.domain.mapper

import com.data.CurrentWeatherJsonResponse
import com.domain.model.CurrentWithLocation
import javax.inject.Inject

class CurrentWithLocationMapper @Inject constructor(
    private val currentWeatherMapper: CurrentWeatherMapper,
    private val locationMapper: LocationMapper
) {

    fun CurrentWeatherJsonResponse.mapToDomain(): CurrentWithLocation {
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
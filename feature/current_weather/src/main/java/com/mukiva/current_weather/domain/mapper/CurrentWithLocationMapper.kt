package com.mukiva.current_weather.domain.mapper

import com.mukiva.current_weather.data.CurrentWeatherJsonResponse
import com.mukiva.current_weather.domain.model.CurrentWithLocation
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
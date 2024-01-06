package com.mukiva.feature.dashboard_impl.domain.mapper

import com.mukiva.feature.dashboard_impl.data.CurrentWeatherRemote
import com.mukiva.feature.dashboard_impl.domain.model.CurrentWithLocation
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
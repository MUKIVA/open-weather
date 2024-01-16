package com.mukiva.openweather.glue.dashboard.mapper

import com.mukiva.core.data.repository.forecast.entity.ICurrentWeatherRemote
import com.mukiva.feature.dashboard.domain.model.CurrentWithLocation
import javax.inject.Inject

class CurrentWithLocationMapper @Inject constructor(
    private val currentWeatherMapper: CurrentWeatherMapper
) {

    fun mapToDomain(item: ICurrentWeatherRemote): CurrentWithLocation = with(item) {
        return CurrentWithLocation(
            currentWeather = currentWeatherMapper.mapToDomain(
                current ?: throw Exception("Fail currentWeather parse")
            ),
            location = LocationMapper.mapToDomain(
                location ?: throw Exception("Fail location parse")
            )


        )
    }

}
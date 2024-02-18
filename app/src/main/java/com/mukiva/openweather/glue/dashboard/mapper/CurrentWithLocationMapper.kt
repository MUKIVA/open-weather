package com.mukiva.openweather.glue.dashboard.mapper

import com.mukiva.core.data.repository.forecast.entity.ForecastWithCurrentAndLocationRemote
import com.mukiva.feature.dashboard.domain.model.CurrentWithLocation
import com.mukiva.openweather.glue.forecast.ForecastMapper
import javax.inject.Inject

class CurrentWithLocationMapper @Inject constructor(
    private val currentWeatherMapper: CurrentWeatherMapper,
    private val forecastMapper: ForecastMapper
) {
    fun mapToDomain(item: ForecastWithCurrentAndLocationRemote): CurrentWithLocation = with(item) {
        return CurrentWithLocation(
            currentWeather = currentWeatherMapper.mapToDomain(
                current ?: throw Exception("Fail currentWeather parse")
            ),
            location = LocationMapper.mapToDomain(
                location ?: throw Exception("Fail location parse")
            ),
            forecastState = forecastMapper.asDomain(
                item.forecast ?: throw Exception("Fail forecast parse")
            )
        )
    }

}
package com.mukiva.openweather.glue.dashboard.mapper

import android.content.Context
import com.mukiva.core.data.entity.CurrentRemote
import com.mukiva.feature.dashboard.domain.model.Condition
import com.mukiva.feature.dashboard.domain.model.CurrentWeather
import com.mukiva.feature.dashboard.domain.model.WindDirection
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import javax.inject.Inject

class CurrentWeatherMapper @Inject constructor(
    @ApplicationContext context: Context
) {

    private val simpleDateFormat = SimpleDateFormat(
        DATE_PATTERN,
        context.resources.configuration.locales[0]
    )

    fun mapToDomain(item: CurrentRemote): CurrentWeather = with(item) {
        return CurrentWeather(
            lastUpdatedEpoch = lastUpdatedEpoch ?: 0,
            lastUpdated = simpleDateFormat.parse(lastUpdated ?: "")
                ?: throw Exception("Can't parse lastUpdated"),
            tempC = tempC?.toFloat() ?: 0.0f,
            tempF = tempF?.toFloat() ?: 0.0f,
            isDay = isDay != 0,
            condition = condition?.let {
                ConditionMapper.mapToDomain(it)
            } ?: Condition.default(),
            windMph = windMph?.toFloat() ?: 0.0f,
            windKph = windKph?.toFloat() ?: 0.0f,
            windDegree = windDegree ?: 0,
            windDir = windDir?.let { WindDirection.valueOf(it) } ?: WindDirection.UNKNOWN,
            pressureMb = pressureMb?.toFloat() ?: 0.0f,
            pressureIn = pressureIn?.toFloat() ?: 0.0f,
            precipMm = precipMm?.toFloat() ?: 0.0f,
            precipIn = precipIn?.toFloat() ?: 0.0f,
            humidity = humidity ?: 0,
            cloud = cloud ?: 0,
            feelsLikeC = feelslikeC?.toFloat() ?: 0.0f,
            feelsLikeF = feelslikeF?.toFloat() ?: 0.0f,
            visKm = visKm?.toFloat() ?: 0.0f,
            visMiles = visKm?.toFloat() ?: 0.0f,
            uv = uv?.toFloat() ?: 0.0f,
            gustMph = gustMph?.toFloat() ?: 0.0f,
            gustKph = gustKph?.toFloat() ?: 0.0f
        )
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd HH:mm"
    }
}
package com.mukiva.openweather.glue.dashboard.mapper

import android.content.Context
import com.mukiva.core.data.entity.CurrentRemote
import com.mukiva.feature.dashboard.domain.model.Condition
import com.mukiva.feature.dashboard.domain.model.CurrentWeather
import com.mukiva.feature.dashboard.domain.model.WindDirection
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

class CurrentWeatherMapper @Inject constructor(
    @ApplicationContext context: Context
) {

    private val simpleDateFormat = SimpleDateFormat(
        DATE_PATTERN,
        context.resources.configuration.locales[0]
    )

    fun mapToDomain(item: CurrentRemote): CurrentWeather {
        return CurrentWeather(
            lastUpdatedEpoch = item.lastUpdatedEpoch ?: 0,
            lastUpdated = simpleDateFormat
                .parse(item.lastUpdated ?: "") ?: Calendar.getInstance().time,
            tempC = item.tempC?.toFloat() ?: 0.0f,
            tempF = item.tempF?.toFloat() ?: 0.0f,
            isDay = item.isDay != 0,
            condition = Condition(
                text = item.condition?.text ?: "",
                icon = item.condition?.icon ?: "",
                code = item.condition?.code ?: 0,
            ),
            windMph= item.windMph?.toFloat() ?: 0.0f,
            windKph = item.windKph?.toFloat() ?: 0.0f,
            windDegree = item.windDegree ?: 0,
            windDir = WindDirection
                .valueOf(item.windDir ?: "UNKNOWN"),
            pressureMb = item.pressureMb?.toFloat() ?: 0.0f,
            pressureIn = item.pressureIn?.toFloat() ?: 0.0f,
            precipMm = item.precipMm?.toFloat() ?: 0.0f,
            precipIn = item.precipIn?.toFloat() ?: 0.0f,
            humidity = item.humidity ?: 0,
            cloud = item.cloud ?: 0,
            feelsLikeC = item.feelslikeC?.toFloat() ?: 0.0f,
            feelsLikeF = item.feelslikeF?.toFloat() ?: 0.0f,
            visKm = item.visKm?.toFloat() ?: 0.0f,
            visMiles = item.visMiles?.toFloat() ?: 0.0f,
            uv = item.uv?.toFloat() ?: 0.0f,
            gustMph = item.gustMph?.toFloat() ?: 0.0f,
            gustKph = item.gustMph?.toFloat() ?: 0.0f,
        )

    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd HH:mm"
    }
}
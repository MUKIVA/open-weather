package com.mukiva.openweather.glue.dashboard.mapper

import android.content.Context
import com.mukiva.core.data.entity.CurrentRemote
import com.mukiva.feature.dashboard.domain.model.Condition
import com.mukiva.feature.dashboard.domain.model.CurrentWeather
import com.mukiva.feature.dashboard.domain.model.WindDirection
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
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
            lastUpdatedEpoch = Clock.System.now().toLocalDateTime(TimeZone.UTC), //item.lastUpdatedEpoch,
            tempC = item.tempC ?: 0.0,
            tempF = item.tempF ?: 0.0,
            isDay = item.isDay != 0,
            condition = Condition(
                text = item.condition?.text ?: "",
                icon = item.condition?.icon ?: "",
                code = item.condition?.code ?: 0,
            ),
            windMph= item.windMph ?: 0.0,
            windKph = item.windKph ?: 0.0,
            windDegree = item.windDegree ?: 0,
            windDir = WindDirection
                .valueOf(item.windDir ?: "UNKNOWN"),
            pressureMb = item.pressureMb ?: 0.0,
            pressureIn = item.pressureIn ?: 0.0,
            precipMm = item.precipMm ?: 0.0,
            precipIn = item.precipIn ?: 0.0,
            humidity = item.humidity ?: 0,
            cloud = item.cloud ?: 0,
            feelsLikeC = item.feelslikeC ?: 0.0,
            feelsLikeF = item.feelslikeF ?: 0.0,
            visKm = item.visKm ?: 0.0,
            visMiles = item.visMiles ?: 0.0,
            uv = item.uv ?: 0.0,
            gustMph = item.gustMph ?: 0.0,
            gustKph = item.gustMph ?: 0.0,
        )

    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd HH:mm"
    }
}
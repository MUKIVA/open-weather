package com.mukiva.openweather.glue.dashboard.mapper

import android.content.Context
import com.mukiva.core.data.entity.CurrentRemote
import com.mukiva.feature.dashboard.domain.model.ICondition
import com.mukiva.feature.dashboard.domain.model.ICurrentWeather
import com.mukiva.feature.dashboard.domain.model.WindDirection
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class CurrentWeatherMapper @Inject constructor(
    @ApplicationContext context: Context
) {

    private val simpleDateFormat = SimpleDateFormat(
        DATE_PATTERN,
        context.resources.configuration.locales[0]
    )

    fun mapToDomain(item: CurrentRemote): ICurrentWeather {
        return object : ICurrentWeather {
            override val lastUpdatedEpoch: Int = item.lastUpdatedEpoch ?: 0
            override val lastUpdated: Date = simpleDateFormat
                .parse(item.lastUpdated ?: "") ?: Calendar.getInstance().time
            override val tempC: Float = item.tempC?.toFloat() ?: 0.0f
            override val tempF: Float = item.tempF?.toFloat() ?: 0.0f
            override val isDay: Boolean = item.isDay != 0
            override val condition: ICondition = object : ICondition {
                override val text: String = item.condition?.text ?: ""
                override val icon: String = item.condition?.icon ?: ""
                override val code: Int = item.condition?.code ?: 0
            }
            override val windMph: Float = item.windMph?.toFloat() ?: 0.0f
            override val windKph: Float = item.windKph?.toFloat() ?: 0.0f
            override val windDegree: Int = item.windDegree ?: 0
            override val windDir: WindDirection = WindDirection
                .valueOf(item.windDir ?: "UNKNOWN")
            override val pressureMb: Float = item.pressureMb?.toFloat() ?: 0.0f
            override val pressureIn: Float = item.pressureIn?.toFloat() ?: 0.0f
            override val precipMm: Float = item.precipMm?.toFloat() ?: 0.0f
            override val precipIn: Float = item.precipIn?.toFloat() ?: 0.0f
            override val humidity: Int = item.humidity ?: 0
            override val cloud: Int = item.cloud ?: 0
            override val feelsLikeC: Float = item.feelslikeC?.toFloat() ?: 0.0f
            override val feelsLikeF: Float = item.feelslikeF?.toFloat() ?: 0.0f
            override val visKm: Float = item.visKm?.toFloat() ?: 0.0f
            override val visMiles: Float = item.visMiles?.toFloat() ?: 0.0f
            override val uv: Float = item.uv?.toFloat() ?: 0.0f
            override val gustMph: Float = item.gustMph?.toFloat() ?: 0.0f
            override val gustKph: Float = item.gustMph?.toFloat() ?: 0.0f
        }

    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd HH:mm"
    }
}
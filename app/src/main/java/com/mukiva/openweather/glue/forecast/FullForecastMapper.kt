package com.mukiva.openweather.glue.forecast

import android.content.Context
import com.mukiva.core.data.repository.forecast.entity.ForecastDayRemote
import com.mukiva.core.data.repository.forecast.entity.ForecastRemote
import com.mukiva.core.data.repository.forecast.entity.HourRemote
import com.mukiva.feature.forecast.domain.ForecastItem
import com.mukiva.feature.forecast.presentation.HourlyForecast
import com.mukiva.feature.forecast.domain.WindDirection
import com.mukiva.feature.forecast.presentation.ForecastGroup
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class FullForecastMapper @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val mTimeFormatter = SimpleDateFormat(
        "yyyy-MM-dd HH:mm",
        context.resources.configuration.locales[0]
    )

    fun asDomain(forecast: ForecastRemote): List<HourlyForecast> {
        return forecast.forecastDay.mapIndexed { index, item ->
            HourlyForecast(
                id = index, 
                date = item.date ?: throw Exception("Fail to get date"),
                groups = createGroup(item)
            )
        }
    }
    
    private fun createGroup(
        item: ForecastDayRemote
    ): List<ForecastGroup> {
        val tempGroup = ArrayList<ForecastItem.HourlyTemp>(HOURS_IN_DAY)
        val windGroup = ArrayList<ForecastItem.HourlyWind>(HOURS_IN_DAY)
        val pressureGroup = ArrayList<ForecastItem.HourlyPressure>(HOURS_IN_DAY)
        val humidityGroup = ArrayList<ForecastItem.HourlyHumidity>(HOURS_IN_DAY)
        
        item.hour.forEach { hourRemote ->
            val date = hourRemote.time?.let { mTimeFormatter.parse(it) } ?: Date()
            tempGroup.add(hourRemote.asTempItem(date))
            windGroup.add(hourRemote.asWindItem(date))
            pressureGroup.add(hourRemote.asPressureItem(date))
            humidityGroup.add(hourRemote.asHumidityItem(date))
        }

        return listOf(
            ForecastGroup(ForecastGroup.Type.TEMP, tempGroup),
            ForecastGroup(ForecastGroup.Type.WIND, windGroup),
            ForecastGroup(ForecastGroup.Type.PRESSURE, pressureGroup),
            ForecastGroup(ForecastGroup.Type.HUMIDITY, humidityGroup),
        )
    }

    private fun HourRemote.asHumidityItem(
        date: Date,
    ): ForecastItem.HourlyHumidity {
        return ForecastItem.HourlyHumidity(
            humidity = humidity ?: 0,
            date = date,
        )
    }

    private fun HourRemote.asPressureItem(
        date: Date,
    ): ForecastItem.HourlyPressure {
        return ForecastItem.HourlyPressure(
            pressureMb = pressureMb ?: 0.0,
            pressureIn = pressureIn ?: 0.0,
            date = date,
        )
    }

    private fun HourRemote.asWindItem(
        date: Date,
    ): ForecastItem.HourlyWind {
        return ForecastItem.HourlyWind(
            windMph = windMph ?: 0.0,
            windKph = windKph ?: 0.0,
            windDegree = windDegree ?: 0,
            windDirection = WindDirection.valueOf(windDir ?: "UNKNOWN"),
            date = date,
        )
    }

    private fun HourRemote.asTempItem(
        date: Date,
    ): ForecastItem.HourlyTemp {
        return ForecastItem.HourlyTemp(
            tempC = tempC ?: 0.0,
            tempF = tempF ?: 0.0,
            feelsLikeC = feelsLikeC ?: 0.0,
            feelsLikeF = feelsLikeF ?: 0.0,
            cloud = cloud ?: 0,
            iconUrl = condition?.icon ?: "",
            date = date,
        )
    }

    companion object {
        private const val HOURS_IN_DAY = 24
    }
}
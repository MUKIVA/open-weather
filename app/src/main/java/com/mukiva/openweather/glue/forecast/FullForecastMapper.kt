@file:Suppress("UNCHECKED_CAST")

package com.mukiva.openweather.glue.forecast

import android.content.Context
import com.mukiva.core.data.repository.forecast.entity.ForecastDayRemote
import com.mukiva.core.data.repository.forecast.entity.ForecastRemote
import com.mukiva.core.data.repository.forecast.entity.HourRemote
import com.mukiva.feature.forecast.domain.IForecastGroup
import com.mukiva.feature.forecast.domain.IForecastItem
import com.mukiva.feature.forecast.domain.IHourlyForecast
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

    fun asDomain(forecast: ForecastRemote): List<IHourlyForecast> {
        return forecast.forecastDay.mapIndexed { index, item ->
            object : IHourlyForecast {
                override val groups: Collection<IForecastGroup<IForecastItem>> = buildList {
                        add(item.getHourlyTemp())
                        add(item.getHourlyWind())
                        add(item.getHourlyPressure())
                        add(item.getHourlyHumidity())
                }
                override val id: Int = index
                override val date: Date = item.date ?: throw Exception("Fail to get date")

            }
        }
    }

    private fun ForecastDayRemote.getHourlyPressure(): IForecastGroup<IForecastItem> {
        return ForecastGroup(
            type = ForecastGroup.GroupType.PRESSURE,
            forecast = createGroup(hour, ForecastGroup.GroupType.PRESSURE)
        )
    }

    private fun ForecastDayRemote.getHourlyHumidity(): IForecastGroup<IForecastItem> {
        return ForecastGroup(
            type = ForecastGroup.GroupType.HUMIDITY,
            forecast = createGroup(hour, ForecastGroup.GroupType.HUMIDITY)
        )
    }

    private fun ForecastDayRemote.getHourlyWind(): IForecastGroup<IForecastItem> {
        return ForecastGroup(
            type = ForecastGroup.GroupType.WIND,
            forecast = createGroup(hour, ForecastGroup.GroupType.WIND)
        )
    }


    private fun ForecastDayRemote.getHourlyTemp(): IForecastGroup<IForecastItem> {
        return ForecastGroup(
            type = ForecastGroup.GroupType.TEMP,
            forecast = createGroup(hour, ForecastGroup.GroupType.TEMP)
        )
    }

    private fun <T : IForecastItem> createGroup(
        items: Collection<HourRemote>,
        type: ForecastGroup.GroupType
    ): IForecastGroup<T> {
        return object : IForecastGroup<T> {
            override val items: Collection<T> = when(type) {
                    ForecastGroup.GroupType.TEMP -> createTempObjects(items)
                    ForecastGroup.GroupType.WIND -> createWindObjects(items)
                    ForecastGroup.GroupType.PRESSURE -> createPressureObjects(items)
                    ForecastGroup.GroupType.HUMIDITY -> createHumidityObjects(items)
                }
        }
    }

    private fun <T : IForecastItem> createTempObjects(items: Collection<HourRemote>): Collection<T> =
        items.map {
            object : IForecastItem.IHourlyTemp {
                override val tempC: Double = it.tempC ?: 0.0
                override val tempF: Double = it.tempF ?: 0.0
                override val feelsLikeC: Double = it.feelsLikeC ?: 0.0
                override val feelsLikeF: Double = it.feelsLikeF ?: 0.0
                override val cloud: Double = it.cloud?.toDouble() ?: 0.0
                override val iconUrl: String = it.condition?.icon ?: ""
                override val date: Date = mTimeFormatter
                    .parse(it.time ?: throw Exception("Fail to get time"))!!

            } as T
        }
    private fun <T : IForecastItem> createWindObjects(items: Collection<HourRemote>): Collection<T> =
        items.map {
            object : IForecastItem.IHourlyWind {
                override val windMph: Double = it.windMph ?: 0.0
                override val windKph: Double = it.windKph ?: 0.0
                override val windDegree: Int = it.windDegree ?: 0
                override val windDirection: WindDirection = it.windDir?.let { str -> WindDirection.valueOf(str) } ?: WindDirection.UNKNOWN
                override val date: Date = mTimeFormatter
                    .parse(it.time ?: throw Exception("Fail to get time"))!!

            } as T
        }

    private fun <T : IForecastItem> createHumidityObjects(items: Collection<HourRemote>): Collection<T> =
        items.map {
            object : IForecastItem.IHourlyHumidity {
                override val humidity: Int = it.humidity ?: 0
                override val date: Date = mTimeFormatter
                    .parse(it.time ?: throw Exception("Fail to get time"))!!
            } as T
        }

    private fun <T : IForecastItem> createPressureObjects(items: Collection<HourRemote>): Collection<T> =
        items.map {
            object : IForecastItem.IHourlyPressure {
                override val pressureMb: Double = it.pressureMb ?: 0.0
                override val pressureIn: Double = it.pressureIn ?: 0.0
                override val date: Date = mTimeFormatter
                    .parse(it.time ?: throw Exception("Fail to get time"))!!

            } as T
        }

}
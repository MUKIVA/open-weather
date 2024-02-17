package com.mukiva.openweather.glue.forecast

import android.content.Context
import com.mukiva.core.data.repository.forecast.entity.ForecastDayRemote
import com.mukiva.core.data.repository.forecast.entity.ForecastRemote
import com.mukiva.feature.forecast.domain.IMinimalForecast
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class ForecastMapper @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val mTimeFormatter = SimpleDateFormat(
        "yyyy-MM-dd HH:mm",
        context.resources.configuration.locales[0]
    )

    private val mHourOfDayFormatter = SimpleDateFormat(
        "hh:mm a",
        context.resources.configuration.locales[0]
    )
    private val mCalendar = Calendar.getInstance()

    fun asDomain(item: ForecastRemote): List<IMinimalForecast> = with(item) {
        return forecastDay.mapIndexed { index, item ->
            object : IMinimalForecast {
                override val id: Int
                    get() = index
                override val dayAvgTempC: Double
                    get() = item.getDayAvgTemp(isMetricSystem = true)
                override val dayAvgTempF: Double
                    get() = item.getDayAvgTemp(isMetricSystem = false)
                override val nightAvgTempC: Double
                    get() = item.getNightAvgTemp(isMetricSystem = true)
                override val nightAvgTempF: Double
                    get() = item.getNightAvgTemp(isMetricSystem = false)
                override val date: Date
                    get() = item.date ?: throw Exception("Fail to get date")
                override val conditionIconUrl: String
                    get() = item.day?.condition?.icon ?: ""
            }
        }
    }

    private fun ForecastDayRemote.getDayAvgTemp(isMetricSystem: Boolean): Double {
        val sunrise = mHourOfDayFormatter.parse(astro?.sunrise ?: throw Exception("Fail to parse sunrise time"))

        mCalendar.time = sunrise!!
        val sunriseHour = mCalendar.get(Calendar.HOUR_OF_DAY)

        val hourItem = hour.find {
            mCalendar.time = it.time?.let { time -> mTimeFormatter.parse(time) } ?: mCalendar.time
            mCalendar.get(Calendar.HOUR_OF_DAY) == sunriseHour
        }

        return when(isMetricSystem) {
            true -> hourItem?.tempC ?: 0.0
            false -> hourItem?.tempF ?: 0.0
        }
    }

    private fun ForecastDayRemote.getNightAvgTemp(isMetricSystem: Boolean): Double {
        val moonrise = mHourOfDayFormatter.parse(astro?.moonrise ?: throw Exception("Fail to parse moonrise time"))

        mCalendar.time = moonrise!!
        val moonriseHour = mCalendar.get(Calendar.HOUR_OF_DAY)

        val hourItem = hour.find {
            mCalendar.time = it.time?.let { time -> mTimeFormatter.parse(time) } ?: mCalendar.time
            mCalendar.get(Calendar.HOUR_OF_DAY) == moonriseHour
        }

        return when(isMetricSystem) {
            true -> hourItem?.tempC ?: 0.0
            false -> hourItem?.tempF ?: 0.0
        }
    }

}
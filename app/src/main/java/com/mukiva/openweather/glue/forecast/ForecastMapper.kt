package com.mukiva.openweather.glue.forecast

import android.content.Context
import com.mukiva.core.data.repository.forecast.entity.ForecastDayRemote
import com.mukiva.core.data.repository.forecast.entity.ForecastRemote
import com.mukiva.feature.forecast.domain.Forecast
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Calendar
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

    fun asDomain(item: ForecastRemote): List<Forecast> = with(item) {
        return forecastDay.mapIndexed { index, item ->
            Forecast(
                id = index,
                dayAvgTempC = item.getDayAvgTempC(isMetricSystem = true),
                dayAvgTempF = item.getDayAvgTempC(isMetricSystem = false),
                nightAvgTempC = item.getNightAvgTempC(isMetricSystem = true),
                nightAvgTempF = item.getNightAvgTempC(isMetricSystem = false),
                date = item.date ?: throw Exception("PARSE FAIL")
            )
        }
    }

    private fun ForecastDayRemote.getDayAvgTempC(isMetricSystem: Boolean): Double {
        val sunrise = mHourOfDayFormatter.parse(astro?.sunrise ?: throw Exception("PARSE FAIL"))

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

    private fun ForecastDayRemote.getNightAvgTempC(isMetricSystem: Boolean): Double {
        val moonrise = mHourOfDayFormatter.parse(astro?.moonrise ?: throw Exception("PARSE FAIL"))

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
package com.mukiva.openweather.glue.forecast

import android.content.Context
import com.mukiva.core.data.repository.forecast.entity.ForecastDayRemote
import com.mukiva.core.data.repository.forecast.entity.ForecastRemote
import com.mukiva.feature.dashboard.domain.model.MinimalForecast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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

    fun asDomain(item: ForecastRemote): List<MinimalForecast> = with(item) {
        return forecastDay.mapIndexed { index, item ->
            MinimalForecast(
                id = index,
                avgTempC = item.day?.avgTempC ?: 0.0,
                avgTempF = item.day?.avgTempF ?: 0.0,
                minTempC = item.day?.minTempC ?: 0.0,
                minTempF = item.day?.minTempF ?: 0.0,
                maxTempC = item.day?.maxTempC ?: 0.0,
                maxTempF = item.day?.maxTempF ?: 0.0,
                date = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                conditionIconUrl = item.day?.condition?.icon ?: "",
            )
        }
    }
}
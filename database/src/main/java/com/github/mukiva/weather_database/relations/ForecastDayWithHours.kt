package com.github.mukiva.weather_database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.github.mukiva.weather_database.models.ForecastDayDbo
import com.github.mukiva.weather_database.models.HourDbo

data class ForecastDayWithHours(
    @Embedded
    val forecastDay: ForecastDayDbo,

    @Relation(
        parentColumn = "id",
        entityColumn = "forecast_day_id"
    )
    val hours: List<HourDbo>
)
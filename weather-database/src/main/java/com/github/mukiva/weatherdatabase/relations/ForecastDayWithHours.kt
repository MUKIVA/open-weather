package com.github.mukiva.weatherdatabase.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.github.mukiva.weatherdatabase.models.ForecastDayDbo
import com.github.mukiva.weatherdatabase.models.HourDbo

public data class ForecastDayWithHours(
    @Embedded
    val forecastDay: ForecastDayDbo,

    @Relation(
        parentColumn = "id",
        entityColumn = "forecast_day_id"
    )
    val hours: List<HourDbo>
)

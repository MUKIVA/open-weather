package com.github.mukiva.weather_database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.github.mukiva.weather_database.models.ForecastDayDbo
import com.github.mukiva.weather_database.models.ForecastDbo

data class ForecastWithDays(
    @Embedded
    val forecastDbo: ForecastDbo,

    @Relation(
        parentColumn = "id",
        entityColumn = "forecast_id",
        entity = ForecastDayDbo::class
    )
    val forecastDays: List<ForecastDayWithHours>
)
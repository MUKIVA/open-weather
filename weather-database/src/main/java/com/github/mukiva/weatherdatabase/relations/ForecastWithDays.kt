package com.github.mukiva.weatherdatabase.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.github.mukiva.weatherdatabase.models.ForecastDayDbo
import com.github.mukiva.weatherdatabase.models.ForecastDbo

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

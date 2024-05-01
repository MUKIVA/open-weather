package com.github.mukiva.weather_database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.github.mukiva.weather_database.models.CurrentDbo
import com.github.mukiva.weather_database.models.ForecastDbo
import com.github.mukiva.weather_database.models.ForecastRequestCacheDbo
import com.github.mukiva.weather_database.models.LocationDbo

data class ForecastWithCurrentAndLocation(
    @Embedded
    val cache: ForecastRequestCacheDbo,

    @Relation(
        parentColumn = "current_id",
        entityColumn = "id"
    )
    val current: CurrentDbo,
    @Relation(
        parentColumn = "location_id",
        entityColumn = "id"
    )
    val location: LocationDbo,
    @Relation(
        parentColumn = "forecast_id",
        entityColumn = "id",
        entity = ForecastDbo::class
    )
    val forecast: ForecastWithDays
)
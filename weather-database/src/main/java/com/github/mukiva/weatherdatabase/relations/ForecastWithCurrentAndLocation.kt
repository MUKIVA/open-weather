package com.github.mukiva.weatherdatabase.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.github.mukiva.weatherdatabase.models.CurrentDbo
import com.github.mukiva.weatherdatabase.models.ForecastDbo
import com.github.mukiva.weatherdatabase.models.ForecastRequestCacheDbo
import com.github.mukiva.weatherdatabase.models.LocationDbo

public data class ForecastWithCurrentAndLocation(
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

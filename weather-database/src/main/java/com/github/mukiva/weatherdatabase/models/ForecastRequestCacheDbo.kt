package com.github.mukiva.weatherdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
public data class ForecastRequestCacheDbo(
    @ColumnInfo("location_id")
    @PrimaryKey
    val locationId: Long,
    @ColumnInfo("current_id")
    val currentId: Long,
    @ColumnInfo("forecast_id")
    val forecastId: Long,
)

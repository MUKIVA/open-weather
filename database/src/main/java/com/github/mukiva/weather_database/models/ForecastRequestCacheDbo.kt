package com.github.mukiva.weather_database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ForecastRequestCacheDbo(
    @ColumnInfo("request_query")
    @PrimaryKey
    val requestQuery: String,
    @ColumnInfo("current_id")
    val currentId: Long,
    @ColumnInfo("location_id")
    val locationId: Long,
    @ColumnInfo("forecast_id")
    val forecastId: Long,
)
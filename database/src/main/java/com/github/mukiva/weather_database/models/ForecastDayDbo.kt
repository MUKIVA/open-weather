package com.github.mukiva.weather_database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity
data class ForecastDayDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("date_epoch")
    val dateEpoch: LocalDateTime,
    @ColumnInfo("day")
    @Embedded
    val day: DayDbo,
    @ColumnInfo("astro")
    @Embedded
    val astro: AstroDbo,
    @ColumnInfo("forecast_id")
    val forecastId: Int
)


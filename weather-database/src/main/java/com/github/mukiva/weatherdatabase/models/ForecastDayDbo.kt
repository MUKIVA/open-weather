package com.github.mukiva.weatherdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity
data class ForecastDayDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("date_epoch")
    val dateEpoch: LocalDateTime,
    @Embedded(prefix = "day")
    val day: DayDbo,
    @Embedded(prefix = "astro")
    val astro: AstroDbo,
    @ColumnInfo("forecast_id")
    val forecastId: Long
)

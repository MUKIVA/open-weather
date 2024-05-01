package com.github.mukiva.weather_database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity
data class CurrentDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("last_updated_epoch")
    var lastUpdatedEpoch: LocalDateTime,
    @ColumnInfo("temp_c")
    var tempC: Double,
    @ColumnInfo("temp_f")
    var tempF: Double,
    @ColumnInfo("is_day")
    var isDay: Int,
    @ColumnInfo("condition")
    @Embedded
    var condition: ConditionDbo,
    @ColumnInfo("wind_mph")
    var windMph: Double,
    @ColumnInfo("wind_kph")
    var windKph: Double,
    @ColumnInfo("wind_degree")
    var windDegree: Int,
    @ColumnInfo("wind_dir")
    var windDir: String,
    @ColumnInfo("pressure_mb")
    var pressureMb: Double,
    @ColumnInfo("pressure_in")
    var pressureIn: Double,
    @ColumnInfo("precip_mm")
    var precipMm: Double,
    @ColumnInfo("precip_in")
    var precipIn: Double,
    @ColumnInfo("humidity")
    var humidity: Int,
    @ColumnInfo("cloud")
    var cloud: Int,
    @ColumnInfo("feelslike_c")
    var feelslikeC: Double,
    @ColumnInfo("feelslike_f")
    var feelslikeF: Double,
    @ColumnInfo("vis_km")
    var visKm: Double,
    @ColumnInfo("vis_miles")
    var visMiles: Double,
    @ColumnInfo("uv")
    var uv: Int,
    @ColumnInfo("gust_mph")
    var gustMph: Double,
    @ColumnInfo("gust_kph")
    var gustKph: Double,
)
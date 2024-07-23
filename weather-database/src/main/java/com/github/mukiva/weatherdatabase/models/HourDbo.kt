package com.github.mukiva.weatherdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity
public data class HourDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("time_epoch")
    val timeEpoch: LocalDateTime,
    @ColumnInfo("temp_c")
    val tempC: Double,
    @ColumnInfo("temp_f")
    val tempF: Double,
    @ColumnInfo("is_day")
    val isDay: Int,
    @ColumnInfo("wind_mph")
    val windMph: Double,
    @ColumnInfo("wind_kph")
    val windKph: Double,
    @ColumnInfo("wind_degree")
    val windDegree: Int,
    @ColumnInfo("wind_dir")
    val windDir: String,
    @ColumnInfo("pressure_mb")
    val pressureMb: Double,
    @ColumnInfo("pressure_in")
    val pressureIn: Double,
    @ColumnInfo("precip_mm")
    val precipMm: Double,
    @ColumnInfo("precip_in")
    val precipIn: Double,
    @ColumnInfo("snow_cm")
    val snowCm: Double,
    @ColumnInfo("humidity")
    val humidity: Int,
    @ColumnInfo("cloud")
    val cloud: Int,
    @ColumnInfo("feelslike_c")
    val feelsLikeC: Double,
    @ColumnInfo("feelslike_f")
    val feelsLikeF: Double,
    @ColumnInfo("windchill_c")
    val windChillC: Double,
    @ColumnInfo("windchill_f")
    val windChillF: Double,
    @ColumnInfo("heatindex_c")
    val heatIndexC: Double,
    @ColumnInfo("heatindex_f")
    val heatIndexF: Double,
    @ColumnInfo("dewpoint_c")
    val dewPointC: Double,
    @ColumnInfo("dewpoint_f")
    val dewPointF: Double,
    @ColumnInfo("will_it_rain")
    val willItRain: Int,
    @ColumnInfo("chance_of_rain")
    val chanceOfRain: Int,
    @ColumnInfo("will_it_snow")
    val willItSnow: Int,
    @ColumnInfo("chance_of_snow")
    val chanceOfSnow: Int,
    @ColumnInfo("vis_km")
    val visKm: Double,
    @ColumnInfo("vis_miles")
    val visMiles: Double,
    @ColumnInfo("gust_mph")
    val gustMph: Double,
    @ColumnInfo("gust_kph")
    val gustKph: Double,
    @ColumnInfo("uv")
    val uv: Double,
    @Embedded(prefix = "condition")
    val condition: ConditionDbo,
    @ColumnInfo("forecast_day_id")
    val forecastDayId: Long,
)

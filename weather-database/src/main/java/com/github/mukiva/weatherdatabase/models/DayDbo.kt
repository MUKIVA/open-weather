package com.github.mukiva.weatherdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity
public data class DayDbo(
    @ColumnInfo("maxtemp_c")
    val maxTempC: Double,
    @ColumnInfo("maxtemp_f")
    val maxTempF: Double,
    @ColumnInfo("mintemp_c")
    val minTempC: Double,
    @ColumnInfo("mintemp_f")
    val minTempF: Double,
    @ColumnInfo("avgtemp_c")
    val avgTempC: Double,
    @ColumnInfo("avgtemp_f")
    val avgTempF: Double,
    @ColumnInfo("maxwind_mph")
    val maxWindMph: Double,
    @ColumnInfo("maxwind_kph")
    val maxWindKph: Double,
    @ColumnInfo("totalprecip_mm")
    val totalPrecipMm: Double,
    @ColumnInfo("totalprecip_in")
    val totalPrecipIn: Double,
    @ColumnInfo("totalsnow_cm")
    val totalSnowCm: Double,
    @ColumnInfo("avgvis_km")
    val avgVisKm: Double,
    @ColumnInfo("avgvis_miles")
    val avgVisMiles: Double,
    @ColumnInfo("avghumidity")
    val avgHumidity: Int,
    @ColumnInfo("daily_will_it_rain")
    val dailyWillItRain: Int,
    @ColumnInfo("daily_chance_of_rain")
    val dailyChanceOfRain: Int,
    @ColumnInfo("daily_will_it_snow")
    val dailyWillItSnow: Int,
    @ColumnInfo("daily_chance_of_snow")
    val dailyChanceOfSnow: Int,
    @ColumnInfo("uv")
    val uv: Double,
    @Embedded(prefix = "condition")
    val condition: ConditionDbo
)
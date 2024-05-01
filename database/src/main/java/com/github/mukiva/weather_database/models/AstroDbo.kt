package com.github.mukiva.weather_database.models

import androidx.room.ColumnInfo

data class AstroDbo(
    @ColumnInfo("sunrise")
    val sunrise: String,
    @ColumnInfo("sunset")
    val sunset: String,
    @ColumnInfo("moonrise")
    val moonrise: String,
    @ColumnInfo("moonset")
    val moonset: String,
    @ColumnInfo("moon_phase")
    val moonPhase: String,
    @ColumnInfo("moon_illumination")
    val moonIllumination: Int,
    @ColumnInfo("is_moon_up")
    val isMoonUp: Int,
    @ColumnInfo("is_sun_up")
    val isSunUp: Int
)
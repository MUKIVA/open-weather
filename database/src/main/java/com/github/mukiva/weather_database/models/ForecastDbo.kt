package com.github.mukiva.weather_database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ForecastDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)


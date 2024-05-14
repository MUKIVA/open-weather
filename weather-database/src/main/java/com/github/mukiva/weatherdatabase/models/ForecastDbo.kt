package com.github.mukiva.weatherdatabase.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ForecastDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)


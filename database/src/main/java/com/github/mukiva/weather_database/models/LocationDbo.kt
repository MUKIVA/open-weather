package com.github.mukiva.weather_database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity
data class LocationDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("region")
    val region: String,
    @ColumnInfo("country")
    val country: String,
    @ColumnInfo("lat")
    val lat: Double,
    @ColumnInfo("lon")
    val lon: Double,
    @ColumnInfo("tz_id")
    val tzId: String,
    @ColumnInfo("localtime_epoch")
    val localtimeEpoch: LocalDateTime,
    @ColumnInfo("priority")
    val priority: Int
)
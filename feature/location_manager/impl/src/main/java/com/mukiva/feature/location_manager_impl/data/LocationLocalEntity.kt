package com.mukiva.feature.location_manager_impl.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationLocalEntity(
    @PrimaryKey
    val uid: Int,
    @ColumnInfo(name = "city_name")
    val cityName: String?,
    @ColumnInfo(name = "region_name")
    val regionName: String?,
    @ColumnInfo(name = "lon")
    val lon: Double?,
    @ColumnInfo(name = "lat")
    val lat: Double?,
    @ColumnInfo(name = "country_name")
    val countryName: String?
)
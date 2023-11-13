package com.mukiva.location_search.data

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
    @ColumnInfo(name = "country_name")
    val countryName: String?
)
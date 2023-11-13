package com.mukiva.location_search.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ILocationDAO {

    @Query("SELECT * FROM LocationLocalEntity")
    fun getAll(): List<LocationLocalEntity>

    @Query("SELECT * FROM LocationLocalEntity WHERE uid LIKE :locationId LIMIT 1")
    fun getById(locationId: Int): LocationLocalEntity?

    @Query("SELECT * FROM LocationLocalEntity WHERE city_name LIKE :cityName LIMIT 1")
    fun getByLocationName(cityName: String): LocationLocalEntity

    @Insert
    fun insert(vararg location: LocationLocalEntity)

    @Delete
    fun delete(location: LocationLocalEntity)
}
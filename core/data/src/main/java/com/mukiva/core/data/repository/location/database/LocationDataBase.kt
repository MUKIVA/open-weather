package com.mukiva.core.data.repository.location.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mukiva.core.data.repository.location.entity.LocationLocalEntity

@Database(
    entities = [LocationLocalEntity::class], version = 1,
    exportSchema = false
)
abstract class LocationDataBase : RoomDatabase() {
    abstract fun locationDao() : ILocationDAO
}
package com.mukiva.feature.location_manager_impl.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocationLocalEntity::class], version = 1,
    exportSchema = true
)
abstract class LocationDataBase : RoomDatabase() {
    abstract fun locationDao() : ILocationDAO
}
package com.github.mukiva.weatherdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mukiva.weatherdatabase.models.LocationDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface ILocationDao {
    @Query("SELECT * FROM LocationDbo ORDER BY priority ASC")
    fun getAll(): Flow<List<LocationDbo>>

    @Query("SELECT * FROM LocationDbo WHERE id = :locationId")
    fun getById(locationId: Long): LocationDbo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationDbo): Long

    @Delete
    suspend fun delete(location: LocationDbo)

    @Query("DELETE FROM LocationDbo")
    suspend fun deleteAll()
}

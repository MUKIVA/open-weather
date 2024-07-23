package com.github.mukiva.weatherdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.mukiva.weatherdatabase.models.LocationDbo
import kotlinx.coroutines.flow.Flow

@Dao
public interface ILocationDao {
    @Query("SELECT * FROM LocationDbo ORDER BY priority ASC")
    public fun getAll(): Flow<List<LocationDbo>>

    @Query("SELECT * FROM LocationDbo WHERE id = :locationId")
    public fun getById(locationId: Long): LocationDbo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public suspend fun insert(location: LocationDbo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public suspend fun insert(locations: List<LocationDbo>)

    @Transaction
    public suspend fun updateLocations(locations: List<LocationDbo>) {
        deleteAll()
        insert(locations)
    }

    @Delete
    public suspend fun delete(location: LocationDbo)

    @Query("DELETE FROM LocationDbo")
    public suspend fun deleteAll()
}

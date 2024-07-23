package com.github.mukiva.weatherdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mukiva.weatherdatabase.models.CurrentDbo
import kotlinx.coroutines.flow.Flow

@Dao
public interface ICurrentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public suspend fun insert(current: CurrentDbo): Long

    @Query("SELECT * FROM CurrentDbo WHERE id = :id")
    public fun getById(id: Int): Flow<CurrentDbo>

    @Query("DELETE FROM CurrentDbo")
    public fun clear()
}

package com.github.mukiva.weather_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mukiva.weather_database.models.CurrentDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface ICurrentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(current: CurrentDbo): Long

    @Query("SELECT * FROM CurrentDbo WHERE id = :id")
    fun getById(id: Int): Flow<CurrentDbo>

    @Query("DELETE FROM CurrentDbo")
    fun clear()
}

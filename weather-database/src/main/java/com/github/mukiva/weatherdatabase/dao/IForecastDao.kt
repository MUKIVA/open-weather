package com.github.mukiva.weatherdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.mukiva.weatherdatabase.models.ForecastDayDbo
import com.github.mukiva.weatherdatabase.models.ForecastDbo
import com.github.mukiva.weatherdatabase.models.ForecastRequestCacheDbo
import com.github.mukiva.weatherdatabase.models.HourDbo
import com.github.mukiva.weatherdatabase.relations.ForecastWithCurrentAndLocation
import kotlinx.coroutines.flow.Flow

@Dao
public interface IForecastDao {
    @Insert
    public suspend fun insertForecast(forecast: ForecastDbo): Long

    @Insert
    public suspend fun insertForecastDay(forecastDay: ForecastDayDbo): Long

    @Insert
    public suspend fun insertHour(hour: List<HourDbo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public suspend fun insertCache(cache: ForecastRequestCacheDbo): Long

    @Query("DELETE FROM ForecastRequestCacheDbo")
    public suspend fun cleanCache()

    @Transaction
    @Query("SELECT * FROM ForecastRequestCacheDbo WHERE location_id = :locationId")
    public fun getCache(locationId: Long): Flow<ForecastWithCurrentAndLocation?>
}

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
interface IForecastDao {
    @Insert
    suspend fun insertForecast(forecast: ForecastDbo): Long

    @Insert
    suspend fun insertForecastDay(forecastDay: ForecastDayDbo): Long

    @Insert
    suspend fun insertHour(hour: List<HourDbo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(cache: ForecastRequestCacheDbo): Long

    @Transaction
    @Query("SELECT * FROM ForecastRequestCacheDbo WHERE request_query LIKE :queryRequest")
    fun getCache(queryRequest: String): Flow<ForecastWithCurrentAndLocation?>
}

package com.github.mukiva.weather_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.mukiva.weather_database.models.ForecastDayDbo
import com.github.mukiva.weather_database.models.ForecastDbo
import com.github.mukiva.weather_database.models.ForecastRequestCacheDbo
import com.github.mukiva.weather_database.models.HourDbo
import com.github.mukiva.weather_database.relations.ForecastWithCurrentAndLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface IForecastDao {
    @Insert
    suspend fun insertForecast(forecast: ForecastDbo): Int
    @Insert
    suspend fun insertForecastDay(forecastDay: ForecastDayDbo): Int
    @Insert
    suspend fun insertHour(hour: List<HourDbo>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(cache: ForecastRequestCacheDbo): Int
    @Transaction
    @Query("SELECT * FROM ForecastRequestCacheDbo WHERE request_query LIKE :queryRequest")
    fun getCache(queryRequest: String): Flow<ForecastWithCurrentAndLocation>

}
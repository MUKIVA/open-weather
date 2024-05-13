package com.github.mukiva.weather_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.mukiva.weather_database.dao.ICurrentDao
import com.github.mukiva.weather_database.dao.IForecastDao
import com.github.mukiva.weather_database.dao.ILocationDao
import com.github.mukiva.weather_database.models.CurrentDbo
import com.github.mukiva.weather_database.models.ForecastDayDbo
import com.github.mukiva.weather_database.models.ForecastDbo
import com.github.mukiva.weather_database.models.ForecastRequestCacheDbo
import com.github.mukiva.weather_database.models.HourDbo
import com.github.mukiva.weather_database.models.LocationDbo
import com.github.mukiva.weather_database.utils.Converters

class WeatherDatabase internal constructor(
    private val weatherRoomDatabase: WeatherRoomDatabase
) {
    val locationDao: ILocationDao
        get() = weatherRoomDatabase.locationDao()
    val currentDao: ICurrentDao
        get() = weatherRoomDatabase.currentDao()
    val forecastDao: IForecastDao
        get() = weatherRoomDatabase.forecastDao()
}

@Database(
    entities = [
        CurrentDbo::class,
        ForecastDayDbo::class,
        ForecastDbo::class,
        ForecastRequestCacheDbo::class,
        HourDbo::class,
        LocationDbo::class,
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
internal abstract class WeatherRoomDatabase : RoomDatabase() {
    abstract fun locationDao(): ILocationDao
    abstract fun currentDao(): ICurrentDao
    abstract fun forecastDao(): IForecastDao
}

fun weatherDatabase(
    applicationContext: Context
): WeatherDatabase {
    val weatherRoomDatabase = Room.databaseBuilder(
        context = checkNotNull(applicationContext.applicationContext),
        WeatherRoomDatabase::class.java,
        "Weather"
    ).build()
    return WeatherDatabase(weatherRoomDatabase)
}

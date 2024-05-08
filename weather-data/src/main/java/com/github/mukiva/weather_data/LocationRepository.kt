package com.github.mukiva.weather_data

import com.github.mukiva.weather_api.IWeatherApi
import com.github.mukiva.weather_api.models.LocationDto
import com.github.mukiva.weather_data.models.Location
import com.github.mukiva.weather_data.utils.RequestResult
import com.github.mukiva.weather_data.utils.asRequestResult
import com.github.mukiva.weather_data.utils.map
import com.github.mukiva.weather_database.WeatherDatabase
import com.github.mukiva.weather_database.models.LocationDbo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LocationRepository(
    private val database: WeatherDatabase,
    private val gateway: IWeatherApi,
) {
    fun searchRemote(q: String): Flow<RequestResult<List<Location>>> {
        val remoteRequest = flow { this.emit(gateway.search(q)) }
            .map { result -> result.asRequestResult() }
            .map { requestResult ->
                requestResult.map { locations ->
                    locations.map { it.toLocation() }
                }
            }
        val start = flow<RequestResult<List<Location>>> { emit(RequestResult.InProgress(null)) }
        return merge(start, remoteRequest)
    }

    fun getAllLocal(): Flow<RequestResult<List<Location>>> {
        val localRequest = database.locationDao
            .getAll()
            .map { locationsDbo -> locationsDbo.map { it.toLocation() } }
            .map { locations -> RequestResult.Success(locations) }
        val start = flow<RequestResult<List<Location>>> { emit(RequestResult.InProgress(null)) }
        return merge(start, localRequest)
    }

    suspend fun addLocalLocation(location: Location) {
        database.locationDao
            .insert(location.toDbo())
    }

    suspend fun removeLocalLocation(location: Location) {
        database.locationDao
            .delete(location.toDbo())
    }

    suspend fun removeAllLocations() {
        database.locationDao.deleteAll()
    }
}

internal fun LocationDto.toLocation(): Location {
    return Location(
        id = id.toLong(),
        name = name,
        region = region,
        country = country,
        lat = lat,
        lon = lon,
        tzId = tzId ?: "",
        localtimeEpoch = localtimeEpoch ?: Clock.System.now().toLocalDateTime(TimeZone.UTC),
        priority = 0,
    )
}

internal fun Location.toDbo(): LocationDbo {
    return LocationDbo(
        id = id,
        name = name,
        region = region,
        country = country,
        lat = lat,
        lon = lon,
        tzId = tzId,
        localtimeEpoch = localtimeEpoch,
        priority = priority,
    )
}

internal fun LocationDbo.toLocation(): Location {
    return Location(
        id = id,
        name = name,
        region = region,
        country = country,
        lat = lat,
        lon = lon,
        tzId = tzId,
        localtimeEpoch = localtimeEpoch,
        priority = priority,
    )
}
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

class LocationRepository(
    private val database: WeatherDatabase,
    private val gateway: IWeatherApi,
) {
    suspend fun searchRemote(q: String): Flow<RequestResult<List<Location>>> {
        return flow { this.emit(gateway.search(q)) }
            .map { result -> result.asRequestResult() }
            .map { requestResult -> requestResult.map { locations -> locations.toLocation() } }
    }

    fun getAllLocal(): Flow<RequestResult<List<Location>>> {
        return database.locationDao
            .getAll()
            .map { locationsDbo -> locationsDbo.map { it.toLocation() } }
            .map { locations -> RequestResult.Success(locations) }
    }

    suspend fun addLocalLocation(location: Location) {
        database.locationDao
            .insert(location.toDbo())
    }

    suspend fun removeLocalLocation(location: Location) {
        database.locationDao
            .delete(location.toDbo())
    }
}




internal fun List<LocationDto>.toLocation(): List<Location> {
    return map { it.toLocation() }
}

internal fun List<Location>.toDbo(): List<LocationDbo> {
    return map { it.toDbo() }
}

internal fun List<LocationDbo>.toLocation(): List<Location> {
    return map { it.toLocation() }
}

internal fun LocationDto.toLocation(): Location {
    return Location(
        id = 0,
        name = name,
        region = region,
        country = country,
        lat = lat,
        lon = lon,
        tzId = tzId,
        localtimeEpoch = localtimeEpoch,
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
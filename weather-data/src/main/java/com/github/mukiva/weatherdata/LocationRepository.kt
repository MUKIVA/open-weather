package com.github.mukiva.weatherdata

import com.github.mukiva.openweather.core.domain.settings.Lang
import com.github.mukiva.weatherapi.IWeatherApi
import com.github.mukiva.weatherdata.models.LocationData
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.asRequestResult
import com.github.mukiva.weatherdata.utils.map
import com.github.mukiva.weatherdata.utils.toDbo
import com.github.mukiva.weatherdata.utils.toLocation
import com.github.mukiva.weatherdatabase.WeatherDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import java.util.Locale

public interface ILocationRepository {
    public fun searchRemote(q: String, lang: Lang): Flow<RequestResult<List<LocationData>>>
    public fun searchRemote(lon: Double, lat: Double, lang: Lang): Flow<RequestResult<List<LocationData>>>
    public fun getAllLocal(): Flow<RequestResult<List<LocationData>>>
    public suspend fun addLocalLocation(locationData: LocationData): RequestResult<Unit>
    public suspend fun updateLocations(locationData: List<LocationData>): RequestResult<Unit>
}

internal class LocationRepository(
    private val database: WeatherDatabase,
    private val gateway: IWeatherApi,
) : ILocationRepository {
    override fun searchRemote(q: String, lang: Lang): Flow<RequestResult<List<LocationData>>> {
        val languageCode = if (lang == Lang.SYSTEM) {
            Locale.getDefault().language
        } else {
            lang.code
        }

        val remoteRequest = flow { this.emit(gateway.search(q, languageCode)) }
            .flowOn(Dispatchers.IO)
            .map { result -> result.asRequestResult() }
            .map { requestResult ->
                requestResult.map { locations ->
                    locations.map { it.toLocation() }
                }
            }
        val start = flowOf<RequestResult<List<LocationData>>>(RequestResult.InProgress(null))
        return merge(start, remoteRequest)
    }

    override fun searchRemote(lon: Double, lat: Double, lang: Lang): Flow<RequestResult<List<LocationData>>> {
        return searchRemote(q = "$lat,$lon", lang)
            .flowOn(Dispatchers.IO)
    }

    override fun getAllLocal(): Flow<RequestResult<List<LocationData>>> {
        val localRequest = database.locationDao.getAll()
            .flowOn(Dispatchers.IO)
            .map { locationsDbo -> locationsDbo.map { it.toLocation() } }
            .map { locations -> RequestResult.Success(locations) }
        val start = flowOf<RequestResult<List<LocationData>>>(RequestResult.InProgress(null))

        return merge(start, localRequest)
    }

    override suspend fun addLocalLocation(locationData: LocationData) = wrapTry<Unit> {
        database.locationDao.insert(locationData.toDbo())
    }

    override suspend fun updateLocations(locationData: List<LocationData>): RequestResult<Unit> = wrapTry {
        database.forecastDao.cleanCache()
        database.locationDao.updateLocations(locationData.map { location -> location.toDbo() })
    }

    private suspend fun <T : Any> wrapTry(block: suspend () -> T): RequestResult<T> {
        return try {
            val result = block()
            RequestResult.Success(result)
        } catch (cause: Exception) {
            RequestResult.Error(null, cause)
        }
    }
}

public fun createLocationRepository(
    database: WeatherDatabase,
    api: IWeatherApi
): ILocationRepository = LocationRepository(database, api)

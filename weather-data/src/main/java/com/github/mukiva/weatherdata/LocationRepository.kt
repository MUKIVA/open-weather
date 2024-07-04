package com.github.mukiva.weatherdata

import com.github.mukiva.openweather.core.domain.settings.Lang
import com.github.mukiva.weatherapi.IWeatherApi
import com.github.mukiva.weatherdata.models.Location
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.asRequestResult
import com.github.mukiva.weatherdata.utils.map
import com.github.mukiva.weatherdata.utils.toDbo
import com.github.mukiva.weatherdata.utils.toLocation
import com.github.mukiva.weatherdatabase.WeatherDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import java.util.Locale

interface ILocationRepository {
    fun searchRemote(q: String, lang: Lang): Flow<RequestResult<List<Location>>>
    fun searchRemote(lon: Double, lat: Double, lang: Lang): Flow<RequestResult<List<Location>>>
    fun getAllLocal(): Flow<RequestResult<List<Location>>>
    suspend fun addLocalLocation(location: Location): RequestResult<Unit>
//    suspend fun addLocalLocations(locations: List<Location>): RequestResult<Unit>
//    suspend fun removeAllLocations(): RequestResult<Unit>
    suspend fun updateLocations(locations: List<Location>): RequestResult<Unit>
}

internal class LocationRepository(
    private val database: WeatherDatabase,
    private val gateway: IWeatherApi,
) : ILocationRepository {
    override fun searchRemote(q: String, lang: Lang): Flow<RequestResult<List<Location>>> {
        val languageCode = if (lang == Lang.SYSTEM) {
            Locale.getDefault().language
        } else {
            lang.code
        }

        val remoteRequest = flow { this.emit(gateway.search(q, languageCode)) }
            .map { result -> result.asRequestResult() }
            .map { requestResult ->
                requestResult.map { locations ->
                    locations.map { it.toLocation() }
                }
            }
        val start = flow<RequestResult<List<Location>>> { emit(RequestResult.InProgress(null)) }
        return merge(start, remoteRequest)
    }

    override fun searchRemote(lon: Double, lat: Double, lang: Lang): Flow<RequestResult<List<Location>>> {
        return searchRemote(q = "$lat,$lon", lang)
    }

    override fun getAllLocal(): Flow<RequestResult<List<Location>>> {
        val localRequest = database.locationDao.getAll()
            .map { locationsDbo -> locationsDbo.map { it.toLocation() } }
            .map { locations -> RequestResult.Success(locations) }
        val start = flow<RequestResult<List<Location>>> { emit(RequestResult.InProgress(null)) }
        return merge(start, localRequest)
    }

    override suspend fun addLocalLocation(location: Location) = wrapTry<Unit> {
        database.locationDao
            .insert(location.toDbo())
    }

    override suspend fun updateLocations(locations: List<Location>): RequestResult<Unit> = wrapTry {
        database.locationDao
            .updateLocations(locations.map { location -> location.toDbo() })
    }

//    override suspend fun addLocalLocations(locations: List<Location>): RequestResult<Unit> = wrapTry {
//        database.locationDao
//            .insert(locations.map { location -> location.toDbo() })
//    }

//    override suspend fun removeAllLocations() = wrapTry {
//        database.forecastDao.cleanCache()
//        database.locationDao.deleteAll()
//    }

    private suspend fun <T : Any> wrapTry(block: suspend () -> T): RequestResult<T> {
        return try {
            val result = block()
            RequestResult.Success(result)
        } catch (cause: Exception) {
            RequestResult.Error(null, cause)
        }
    }
}

fun createLocationRepository(
    database: WeatherDatabase,
    api: IWeatherApi
): ILocationRepository = LocationRepository(database, api)

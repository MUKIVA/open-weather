package com.github.mukiva.weather_data

import android.util.Log
import com.github.mukiva.weather_api.IWeatherApi
import com.github.mukiva.weather_api.models.ForecastWithCurrentAndLocationDto
import com.github.mukiva.weather_data.models.ForecastWithCurrentAndLocation
import com.github.mukiva.weather_data.utils.ForecastMergeStrategy
import com.github.mukiva.weather_data.utils.IDataMergeStrategy
import com.github.mukiva.weather_data.utils.RequestResult
import com.github.mukiva.weather_data.utils.asRequestResult
import com.github.mukiva.weather_data.utils.map
import com.github.mukiva.weather_data.utils.toDbo
import com.github.mukiva.weather_data.utils.toDomain
import com.github.mukiva.weather_database.WeatherDatabase
import com.github.mukiva.weather_database.models.ForecastDbo
import com.github.mukiva.weather_database.models.ForecastRequestCacheDbo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import com.github.mukiva.weather_database.relations.ForecastWithCurrentAndLocation as ForecastWithCurrentAndLocationDbo

class ForecastRepository(
    private val database: WeatherDatabase,
    private val gateway: IWeatherApi,
) {
    fun getForecast(
        locationName: String,
        forecastMergeStrategy: IDataMergeStrategy<RequestResult<ForecastWithCurrentAndLocation>> =
            ForecastMergeStrategy()
    ): Flow<RequestResult<ForecastWithCurrentAndLocation>> {
        val remote = getRemoteForecast(locationName)
            .map { requestResult -> requestResult.map { dto -> dto.toDomain() } }
        val local = getLocalForecast(locationName)
            .map { requestResult ->
                requestResult.map { cache ->
                    cache.toDomain()
                }
            }
        Log.d("DATA", "FORECAST")
        return local.combine(remote, forecastMergeStrategy::merge)
    }

    private fun getRemoteForecast(
        locationName: String
    ): Flow<RequestResult<ForecastWithCurrentAndLocationDto>> {
        val remote = flow { emit(gateway.forecast(locationName, FORECAST_DAYS)) }
            .map { result -> result.asRequestResult() }
            .onEach { requestResult -> saveForecastCache(locationName, requestResult) }
        val start = flow<RequestResult<ForecastWithCurrentAndLocationDto>> {
            emit(RequestResult.InProgress(null))
        }
        return merge(start, remote)
    }

    private fun getLocalForecast(
        locationName: String
    ): Flow<RequestResult<ForecastWithCurrentAndLocationDbo>> {
        val local = database.forecastDao
            .getCache(locationName)
            .map(::cacheValidate)

        val start = flow<RequestResult<ForecastWithCurrentAndLocationDbo>> {
            emit(RequestResult.InProgress(null))
        }
        return merge(start, local)
    }

    private suspend fun saveForecastCache(
        requestQuery: String,
        requestResult: RequestResult<ForecastWithCurrentAndLocationDto>,
    ) {
        if (requestResult !is RequestResult.Success) return
        val dto = checkNotNull(requestResult.data)
        val currentDbo = dto.current.toDbo()
        val locationDbo = database.locationDao.getByLocationName(
            cityName = dto.location.name,
            region = dto.location.region
        )
        val currentId = database.currentDao.insert(currentDbo)
        val forecastId = database.forecastDao.insertForecast(ForecastDbo())

        dto.forecast.forecastDay
            .map { forecastDto ->
                val forecastDayId = database.forecastDao
                    .insertForecastDay(forecastDto.toDbo(forecastId))
                forecastDayId to forecastDto.hour
            }
            .onEach { (forecastId, hours) ->
                database.forecastDao
                    .insertHour(hours.map { hour -> hour.toDbo(forecastId) })
            }

        val cache = ForecastRequestCacheDbo(
            requestQuery = requestQuery,
            currentId = currentId,
            locationId = locationDbo.id,
            forecastId = forecastId,
        )
        database.forecastDao.insertCache(cache)
    }

    private fun cacheValidate(
        cache: ForecastWithCurrentAndLocationDbo?
    ): RequestResult<ForecastWithCurrentAndLocationDbo> {
        return if (cache == null) {
            RequestResult.InProgress(null)
        } else {
            RequestResult.Success(cache)
        }
    }

    companion object {
        private const val FORECAST_DAYS = 3
    }
}

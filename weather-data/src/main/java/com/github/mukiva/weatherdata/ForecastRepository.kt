package com.github.mukiva.weatherdata

import com.github.mukiva.openweather.core.domain.settings.Lang
import com.github.mukiva.weatherapi.IWeatherApi
import com.github.mukiva.weatherapi.models.ForecastWithCurrentAndLocationDto
import com.github.mukiva.weatherdata.models.ForecastWithCurrentAndLocationData
import com.github.mukiva.weatherdata.utils.ForecastMergeStrategy
import com.github.mukiva.weatherdata.utils.IDataMergeStrategy
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.asRequestResult
import com.github.mukiva.weatherdata.utils.map
import com.github.mukiva.weatherdata.utils.toDbo
import com.github.mukiva.weatherdata.utils.toDomain
import com.github.mukiva.weatherdatabase.WeatherDatabase
import com.github.mukiva.weatherdatabase.models.ForecastDbo
import com.github.mukiva.weatherdatabase.models.ForecastRequestCacheDbo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import java.util.Locale
import com.github.mukiva.weatherdatabase.relations.ForecastWithCurrentAndLocation as ForecastWithCurrentAndLocationDbo

public interface IForecastRepository {
    public fun getForecast(
        locationId: Long,
        lang: Lang,
        onlyCache: Boolean = false,
        forecastMergeStrategy: IDataMergeStrategy<RequestResult<ForecastWithCurrentAndLocationData>> =
            ForecastMergeStrategy()
    ): Flow<RequestResult<ForecastWithCurrentAndLocationData>>
}

internal class ForecastRepository(
    private val database: WeatherDatabase,
    private val gateway: IWeatherApi,
) : IForecastRepository {
    override fun getForecast(
        locationId: Long,
        lang: Lang,
        onlyCache: Boolean,
        forecastMergeStrategy: IDataMergeStrategy<RequestResult<ForecastWithCurrentAndLocationData>>
    ): Flow<RequestResult<ForecastWithCurrentAndLocationData>> = when (onlyCache) {
        true -> getLocalForecast(locationId)
        false -> {
            val remote = getRemoteForecast(locationId, lang)
            val local = getLocalForecast(locationId)
            local.combine(remote, forecastMergeStrategy::merge)
        }
    }

    private fun getRemoteForecast(
        locationId: Long,
        lang: Lang,
    ): Flow<RequestResult<ForecastWithCurrentAndLocationData>> {
        val languageCode = if (lang == Lang.SYSTEM) {
            Locale.getDefault().language
        } else {
            lang.code
        }

        val remote = flow { emit(gateway.forecast("id:$locationId", FORECAST_DAYS, languageCode)) }
            .map { result -> result.asRequestResult() }
            .onEach { saveForecastCache(locationId, it) }
        val start = flowOf<RequestResult<ForecastWithCurrentAndLocationDto>>(
            RequestResult.InProgress(null)
        )

        return merge(start, remote)
            .flowOn(Dispatchers.IO)
            .map { requestResult -> requestResult.map { dto -> dto.toDomain() } }
    }

    private fun getLocalForecast(
        locationId: Long
    ): Flow<RequestResult<ForecastWithCurrentAndLocationData>> {
        val local = database.forecastDao
            .getCache(locationId)
            .flowOn(Dispatchers.IO)
            .map(::cacheValidate)

        val start = flowOf<RequestResult<ForecastWithCurrentAndLocationDbo>>(
            RequestResult.InProgress(null)
        )

        return merge(start, local)
            .flowOn(Dispatchers.IO)
            .map { requestResult ->
                requestResult.map { cache ->
                    cache.toDomain()
                }
            }
    }

    private suspend fun saveForecastCache(
        locationId: Long,
        requestResult: RequestResult<ForecastWithCurrentAndLocationDto>,
    ) {
        if (requestResult !is RequestResult.Success) return
        val dto = checkNotNull(requestResult.data)
        val currentDbo = dto.current.toDbo()
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
            currentId = currentId,
            locationId = locationId,
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

public fun createForecastRepository(
    database: WeatherDatabase,
    gateway: IWeatherApi,
): IForecastRepository = ForecastRepository(database, gateway)

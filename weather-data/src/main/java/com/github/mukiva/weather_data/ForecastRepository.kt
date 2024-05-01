package com.github.mukiva.weather_data

import com.github.mukiva.weather_api.IWeatherApi
import com.github.mukiva.weather_api.models.AstroDto
import com.github.mukiva.weather_api.models.ConditionDto
import com.github.mukiva.weather_api.models.CurrentDto
import com.github.mukiva.weather_api.models.DayDto
import com.github.mukiva.weather_api.models.ForecastDayDto
import com.github.mukiva.weather_api.models.ForecastDto
import com.github.mukiva.weather_api.models.ForecastWithCurrentAndLocationDto
import com.github.mukiva.weather_api.models.HourDto
import com.github.mukiva.weather_data.models.Astro
import com.github.mukiva.weather_data.models.Condition
import com.github.mukiva.weather_data.models.Current
import com.github.mukiva.weather_data.models.Day
import com.github.mukiva.weather_data.models.Forecast
import com.github.mukiva.weather_data.models.ForecastDay
import com.github.mukiva.weather_data.models.ForecastWithCurrentAndLocation
import com.github.mukiva.weather_data.models.Hour
import com.github.mukiva.weather_data.utils.ForecastMergeStrategy
import com.github.mukiva.weather_data.utils.IDataMergeStrategy
import com.github.mukiva.weather_data.utils.RequestResult
import com.github.mukiva.weather_data.utils.asRequestResult
import com.github.mukiva.weather_data.utils.map
import com.github.mukiva.weather_database.WeatherDatabase
import com.github.mukiva.weather_database.models.AstroDbo
import com.github.mukiva.weather_database.models.ConditionDbo
import com.github.mukiva.weather_database.models.CurrentDbo
import com.github.mukiva.weather_database.models.DayDbo
import com.github.mukiva.weather_database.models.ForecastDayDbo
import com.github.mukiva.weather_database.models.ForecastDbo
import com.github.mukiva.weather_database.models.ForecastRequestCacheDbo
import com.github.mukiva.weather_database.models.HourDbo
import com.github.mukiva.weather_database.relations.ForecastDayWithHours
import com.github.mukiva.weather_database.relations.ForecastWithDays
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
    suspend fun getForecast(
        locationName: String,
        forecastMergeStrategy: IDataMergeStrategy<RequestResult<ForecastWithCurrentAndLocation>> = ForecastMergeStrategy()
    ): Flow<RequestResult<ForecastWithCurrentAndLocation>> {
        val local = getLocalForecast(locationName)
            .map { requestResult -> requestResult.map { cache -> cache.toDomain() } }
        val remote = getRemoteForecast(locationName)
            .map { requestResult -> requestResult.map { dto -> dto.toDomain() } }

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
            .map { cache -> RequestResult.Success(cache) }

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

    companion object {
        private const val FORECAST_DAYS = 3
    }
}

internal fun ForecastWithCurrentAndLocationDto.toDomain(): ForecastWithCurrentAndLocation {
    return ForecastWithCurrentAndLocation(
        location = location.toLocation(),
        current = current.toCurrent(),
        forecast = forecast.toForecast(),
    )
}

internal fun ForecastWithCurrentAndLocationDbo.toDomain(): ForecastWithCurrentAndLocation {
    return ForecastWithCurrentAndLocation(
        location = location.toLocation(),
        current = current.toCurrent(),
        forecast = forecast.toForecast(),
    )
}

internal fun ForecastDto.toForecast(): Forecast {
    return Forecast(
        forecastDay = forecastDay.map { it.toForecastDay() },
    )
}

internal fun ForecastWithDays.toForecast(): Forecast {
    return Forecast(
        forecastDay = forecastDays.map { it.toForecastDay() },
    )
}

internal fun ForecastDayWithHours.toForecastDay(): ForecastDay {
    return ForecastDay(
        dateEpoch = forecastDay.dateEpoch,
        day = forecastDay.day.toDay(),
        astro = forecastDay.astro.toAstro(),
        hour = hours.map { it.toHour() },
    )

}

internal fun AstroDbo.toAstro(): Astro {
    return Astro(
        sunrise = sunrise,
        sunset = sunset,
        moonrise = moonrise,
        moonset = moonset,
        moonPhase = moonPhase,
        moonIllumination = moonIllumination,
        isMoonUp = isMoonUp,
        isSunUp = isSunUp,
    )
}

internal fun DayDbo.toDay(): Day {
    return Day(
        maxTempC = maxTempC,
        maxTempF = maxTempF,
        minTempC = minTempC,
        minTempF = minTempF,
        avgTempC = avgTempC,
        avgTempF = avgTempF,
        maxWindMph = maxWindMph,
        maxWindKph = maxWindKph,
        totalPrecipMm = totalPrecipMm,
        totalPrecipIn = totalPrecipIn,
        totalSnowCm = totalSnowCm,
        avgVisKm = avgVisKm,
        avgVisMiles = avgVisMiles,
        avgHumidity = avgHumidity,
        dailyWillItRain = dailyWillItRain,
        dailyChanceOfRain = dailyChanceOfRain,
        dailyWillItSnow = dailyWillItSnow,
        dailyChanceOfSnow = dailyChanceOfSnow,
        uv = uv,
        condition = condition.toCondition(),
    )
}

internal fun DayDto.toDay(): Day {
    return Day(
        maxTempC = maxTempC,
        maxTempF = maxTempF,
        minTempC = minTempC,
        minTempF = minTempF,
        avgTempC = avgTempC,
        avgTempF = avgTempF,
        maxWindMph = maxWindMph,
        maxWindKph = maxWindKph,
        totalPrecipMm = totalPrecipMm,
        totalPrecipIn = totalPrecipIn,
        totalSnowCm = totalSnowCm,
        avgVisKm = avgVisKm,
        avgVisMiles = avgVisMiles,
        avgHumidity = avgHumidity,
        dailyWillItRain = dailyWillItRain,
        dailyChanceOfRain = dailyChanceOfRain,
        dailyWillItSnow = dailyWillItSnow,
        dailyChanceOfSnow = dailyChanceOfSnow,
        uv = uv,
        condition = condition.toCondition(),
    )
}

internal fun AstroDto.toAstro(): Astro {
    return Astro(
        sunrise = sunrise,
        sunset = sunset,
        moonrise = moonrise,
        moonset = moonset,
        moonPhase = moonPhase,
        moonIllumination = moonIllumination,
        isMoonUp = isMoonUp,
        isSunUp = isSunUp,
    )
}

internal fun CurrentDto.toCurrent(): Current {
    return Current(
        lastUpdatedEpoch = lastUpdatedEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        condition = condition.toCondition(),
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        humidity = humidity,
        cloud = cloud,
        feelslikeC = feelslikeC,
        feelslikeF = feelslikeF,
        visKm = visKm,
        visMiles = visMiles,
        uv = uv,
        gustMph = gustMph,
        gustKph = gustKph,
    )
}


internal fun CurrentDbo.toCurrent(): Current {
    return Current(
        lastUpdatedEpoch = lastUpdatedEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        condition = condition.toCondition(),
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        humidity = humidity,
        cloud = cloud,
        feelslikeC = feelslikeC,
        feelslikeF = feelslikeF,
        visKm = visKm,
        visMiles = visMiles,
        uv = uv,
        gustMph = gustMph,
        gustKph = gustKph,
    )
}

internal fun HourDto.toDbo(forecastDayId: Int): HourDbo {
    return HourDbo(
        id = 0,
        timeEpoch = timeEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        snowCm = snowCm,
        humidity = humidity,
        cloud = cloud,
        feelsLikeC = feelsLikeC,
        feelsLikeF = feelsLikeF,
        windChillC = windChillC,
        windChillF = windChillF,
        heatIndexC = heatIndexC,
        heatIndexF = heatIndexF,
        dewPointC = dewPointC,
        dewPointF = dewPointF,
        willItRain = willItRain,
        chanceOfRain = chanceOfRain,
        willItSnow = willItSnow,
        chanceOfSnow = chanceOfSnow,
        visKm = visKm,
        visMiles = visMiles,
        gustMph = gustMph,
        gustKph = gustKph,
        uv = uv,
        condition = condition.toDbo(),
        forecastDayId = forecastDayId,
    )
}

internal fun HourDto.toHour(): Hour {
    return Hour(
        timeEpoch = timeEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        snowCm = snowCm,
        humidity = humidity,
        cloud = cloud,
        feelsLikeC = feelsLikeC,
        feelsLikeF = feelsLikeF,
        windChillC = windChillC,
        windChillF = windChillF,
        heatIndexC = heatIndexC,
        heatIndexF = heatIndexF,
        dewPointC = dewPointC,
        dewPointF = dewPointF,
        willItRain = willItRain,
        chanceOfRain = chanceOfRain,
        willItSnow = willItSnow,
        chanceOfSnow = chanceOfSnow,
        visKm = visKm,
        visMiles = visMiles,
        gustMph = gustMph,
        gustKph = gustKph,
        uv = uv,
        condition = condition.toCondition()
    )
}

internal fun HourDbo.toHour(): Hour {
    return Hour(
        timeEpoch = timeEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        snowCm = snowCm,
        humidity = humidity,
        cloud = cloud,
        feelsLikeC = feelsLikeC,
        feelsLikeF = feelsLikeF,
        windChillC = windChillC,
        windChillF = windChillF,
        heatIndexC = heatIndexC,
        heatIndexF = heatIndexF,
        dewPointC = dewPointC,
        dewPointF = dewPointF,
        willItRain = willItRain,
        chanceOfRain = chanceOfRain,
        willItSnow = willItSnow,
        chanceOfSnow = chanceOfSnow,
        visKm = visKm,
        visMiles = visMiles,
        gustMph = gustMph,
        gustKph = gustKph,
        uv = uv,
        condition = condition.toCondition()
    )
}

internal fun ForecastDayDto.toDbo(forecastId: Int): ForecastDayDbo {
    return ForecastDayDbo(
        id = 0,
        dateEpoch = dateEpoch,
        day = day.toDbo(),
        astro = astro.toDbo(),
        forecastId = forecastId
    )
}

internal fun ForecastDayDto.toForecastDay(): ForecastDay {
    return ForecastDay(
        dateEpoch = dateEpoch,
        day = day.toDay(),
        astro = astro.toAstro(),
        hour = hour.map { it.toHour() },
    )
}

internal fun AstroDto.toDbo(): AstroDbo {
    return AstroDbo(
        sunrise = sunrise,
        sunset = sunset,
        moonrise = moonrise,
        moonset = moonset,
        moonPhase = moonPhase,
        moonIllumination = moonIllumination,
        isMoonUp = isMoonUp,
        isSunUp = isSunUp
    )
}

internal fun DayDto.toDbo(): DayDbo {
    return DayDbo(
        id = 0,
        maxTempC = maxTempC,
        maxTempF = maxTempF,
        minTempC = minTempC,
        minTempF = minTempF,
        avgTempC = avgTempC,
        avgTempF = avgTempF,
        maxWindMph = maxWindMph,
        maxWindKph = maxWindKph,
        totalPrecipMm = totalPrecipMm,
        totalPrecipIn = totalPrecipIn,
        totalSnowCm = totalSnowCm,
        avgVisKm = avgVisKm,
        avgVisMiles = avgVisMiles,
        avgHumidity = avgHumidity,
        dailyWillItRain = dailyWillItRain,
        dailyChanceOfRain = dailyChanceOfRain,
        dailyWillItSnow = dailyWillItSnow,
        dailyChanceOfSnow = dailyChanceOfSnow,
        uv = uv,
        condition = condition.toDbo()
    )
}

internal fun CurrentDto.toDbo(): CurrentDbo {
    return CurrentDbo(
        id = 0,
        lastUpdatedEpoch = lastUpdatedEpoch,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        condition = condition.toDbo(),
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        humidity = humidity,
        cloud = cloud,
        feelslikeC = feelslikeC,
        feelslikeF = feelslikeF,
        visKm = visKm,
        visMiles = visMiles,
        uv = uv,
        gustMph = gustMph,
        gustKph = gustKph,
    )
}

internal fun ConditionDto.toDbo(): ConditionDbo {
    return ConditionDbo(
        text = text,
        icon = icon,
        code = code
    )
}

internal fun ConditionDto.toCondition(): Condition {
    return Condition(
        text = text,
        icon = icon,
        code = code,
    )
}

internal fun ConditionDbo.toCondition(): Condition {
    return Condition(
        text = text,
        icon = icon,
        code = code,
    )
}
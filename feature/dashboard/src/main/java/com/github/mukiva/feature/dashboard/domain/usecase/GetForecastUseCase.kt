package com.github.mukiva.feature.dashboard.domain.usecase

import com.github.mukiva.feature.dashboard.domain.model.Condition
import com.github.mukiva.feature.dashboard.domain.model.Forecast
import com.github.mukiva.feature.dashboard.domain.model.MinimalForecast
import com.github.mukiva.openweather.core.domain.settings.UnitsType
import com.github.mukiva.openweather.core.domain.weather.Distance
import com.github.mukiva.openweather.core.domain.weather.Precipitation
import com.github.mukiva.openweather.core.domain.weather.Pressure
import com.github.mukiva.openweather.core.domain.weather.Speed
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.openweather.core.domain.weather.WindDirection
import com.github.mukiva.weatherdata.ForecastRepository
import com.github.mukiva.weatherdata.SettingsRepository
import com.github.mukiva.weatherdata.models.Current
import com.github.mukiva.weatherdata.models.ForecastDay
import com.github.mukiva.weatherdata.models.ForecastWithCurrentAndLocation
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import com.github.mukiva.weatherdata.models.Condition as DataCondition
import com.github.mukiva.weatherdata.models.Forecast as DataForecast

class GetForecastUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(id: Long): Flow<RequestResult<Forecast>> {
        val lang = settingsRepository
            .getLocalization()
            .first()
        val request = forecastRepository.getForecast(id, lang)
        val settings = settingsRepository.getUnitsType()
        return settings.combine(request) { unitsType, requestResult ->
            requestResult.map { forecast -> toForecast(unitsType, forecast) }
        }
    }

    private fun toForecast(
        unitsType: UnitsType,
        forecast: ForecastWithCurrentAndLocation,
    ): Forecast {
        return Forecast(
            locationName = forecast.location.name,
            currentWeather = toCurrent(forecast.current, unitsType),
            forecastState = toForecast(forecast.current.isDay, forecast.forecast, unitsType),
        )
    }

    private fun toForecast(
        isDay: Int,
        forecast: DataForecast,
        unitsType: UnitsType,
    ): List<MinimalForecast> {
        return forecast.forecastDay.mapIndexed { index, forecastDay ->
            toMinimalForecast(index, isDay, forecastDay, unitsType)
        }
    }

    private fun toMinimalForecast(
        index: Int,
        isDay: Int,
        forecastDay: ForecastDay,
        unitsType: UnitsType,
    ): MinimalForecast = with(forecastDay) {
        return MinimalForecast(
            id = index,
            avgTemp = Temp(unitsType, day.avgTempC, day.avgTempC),
            minTemp = Temp(unitsType, day.minTempC, day.minTempF),
            maxTemp = Temp(unitsType, day.maxTempC, day.maxTempF),
            conditionIconCode = forecastDay.day.condition.code,
            isDay = isDay == 1,
            date = forecastDay.dateEpoch,
        )
    }

    private fun toCurrent(
        current: Current,
        unitsType: UnitsType,
    ): com.github.mukiva.feature.dashboard.domain.model.CurrentWeather = with(current) {
        return com.github.mukiva.feature.dashboard.domain.model.CurrentWeather(
            lastUpdatedEpoch = lastUpdatedEpoch,
            temp = Temp(unitsType, tempC, tempF),
            isDay = isDay == 1,
            condition = toCondition(condition),
            windSpeed = Speed(unitsType, windKph, windMph),
            windDegree = windDegree,
            windDir = WindDirection.valueOf(current.windDir),
            pressure = Pressure(unitsType, pressureMb, pressureIn),
            precip = Precipitation(unitsType, precipMm, precipIn),
            humidity = current.humidity,
            cloud = current.cloud,
            feelsLike = Temp(unitsType, feelslikeC, feelslikeF),
            vis = Distance(unitsType, visKm, visMiles),
            uv = current.uv,
            gust = Speed(unitsType, gustKph, gustMph),
        )
    }

    private fun toCondition(condition: DataCondition): Condition {
        return Condition(
            text = condition.text,
            icon = condition.icon,
            code = condition.code,
        )
    }
}

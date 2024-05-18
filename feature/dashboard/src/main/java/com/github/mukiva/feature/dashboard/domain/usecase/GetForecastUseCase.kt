package com.github.mukiva.feature.dashboard.domain.usecase

import android.util.Log
import com.github.mukiva.feature.dashboard.domain.model.Condition
import com.github.mukiva.feature.dashboard.domain.model.Forecast
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.github.mukiva.weatherdata.models.Condition as DataCondition
import com.github.mukiva.weatherdata.models.Forecast as DataForecast

class GetForecastUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository,
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(id: Long): Flow<RequestResult<Forecast>> {
        val request = forecastRepository.getForecast(id)
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
            forecastState = toForecast(forecast.forecast, unitsType),
        )
    }

    private fun toForecast(
        forecast: DataForecast,
        unitsType: UnitsType,
    ): List<com.github.mukiva.feature.dashboard.domain.model.MinimalForecast> {
        return forecast.forecastDay.mapIndexed { index, forecastDay ->
            toMinimalForecast(index, forecastDay, unitsType)
        }
    }

    private fun toMinimalForecast(
        index: Int,
        forecastDay: ForecastDay,
        unitsType: UnitsType,
    ): com.github.mukiva.feature.dashboard.domain.model.MinimalForecast = with(forecastDay) {
        return com.github.mukiva.feature.dashboard.domain.model.MinimalForecast(
            id = index,
            avgTemp = Temp(unitsType, day.avgTempC, day.avgTempC),
            minTemp = Temp(unitsType, day.minTempC, day.minTempF),
            maxTemp = Temp(unitsType, day.maxTempC, day.maxTempF),
            conditionIconUrl = forecastDay.day.condition.icon,
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

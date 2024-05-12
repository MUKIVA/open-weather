package com.mukiva.feature.dashboard.domain.usecase

import com.github.mukiva.open_weather.core.domain.Distance
import com.github.mukiva.open_weather.core.domain.Precipitation
import com.github.mukiva.open_weather.core.domain.Pressure
import com.github.mukiva.open_weather.core.domain.Speed
import com.github.mukiva.open_weather.core.domain.Temp
import com.github.mukiva.open_weather.core.domain.UnitsType
import com.github.mukiva.open_weather.core.domain.WindDirection
import com.github.mukiva.weather_data.ForecastRepository
import com.github.mukiva.weather_data.models.Current
import com.github.mukiva.weather_data.models.ForecastDay
import com.github.mukiva.weather_data.models.ForecastWithCurrentAndLocation
import com.github.mukiva.weather_data.utils.RequestResult
import com.github.mukiva.weather_data.utils.map
import com.mukiva.feature.dashboard.domain.model.Condition
import com.mukiva.feature.dashboard.domain.model.CurrentWeather
import com.mukiva.feature.dashboard.domain.model.Forecast
import com.mukiva.feature.dashboard.domain.model.MinimalForecast
import com.mukiva.feature.dashboard.domain.repository.ISettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import com.github.mukiva.weather_data.models.Condition as DataCondition
import com.github.mukiva.weather_data.models.Forecast as DataForecast

class GetForecastUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository,
    private val settings: ISettingsRepository
) {
    operator fun invoke(location: String): Flow<RequestResult<Forecast>> {
        val request = forecastRepository.getForecast(location)
        val settings = settings.getUnitsTypeFlow()

        return settings.combine(request) { unitsType, requestResult ->
            requestResult.map { forecast -> toForecast(unitsType, forecast) }
        }
    }

    private fun toForecast(
        unitsType: UnitsType,
        forecast: ForecastWithCurrentAndLocation,
    ): Forecast {
        return Forecast(
            currentWeather = toCurrent(forecast.current, unitsType),
            forecastState = toForecast(forecast.forecast, unitsType),
        )
    }

    private fun toForecast(
        forecast: DataForecast,
        unitsType: UnitsType,
    ): List<MinimalForecast> {
        return forecast.forecastDay.mapIndexed { index, forecastDay ->
            toMinimalForecast(index, forecastDay, unitsType)
        }
    }

    private fun toMinimalForecast(
        index: Int,
        forecastDay: ForecastDay,
        unitsType: UnitsType,
    ): MinimalForecast = with(forecastDay) {
        return MinimalForecast(
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
    ): CurrentWeather = with(current) {
        return CurrentWeather(
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
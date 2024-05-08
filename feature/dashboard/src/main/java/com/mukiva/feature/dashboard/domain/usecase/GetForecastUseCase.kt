package com.mukiva.feature.dashboard.domain.usecase

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
import com.mukiva.feature.dashboard.domain.model.WindDirection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.github.mukiva.weather_data.models.Condition as DataCondition
import com.github.mukiva.weather_data.models.Forecast as DataForecast

class GetForecastUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository,
) {
    operator fun invoke(location: String): Flow<RequestResult<Forecast>> {
        return forecastRepository.getForecast(location)
                .map { requestResult: RequestResult<ForecastWithCurrentAndLocation> ->
                    requestResult.map(::toForecast)
                }

    }

    private fun toForecast(forecast: ForecastWithCurrentAndLocation): Forecast {
        return Forecast(
            currentWeather = toCurrent(forecast.current),
            forecastState = toForecast(forecast.forecast),
        )
    }

    private fun toForecast(forecast: DataForecast): List<MinimalForecast> {
        return forecast.forecastDay.mapIndexed(::toMinimalForecast)
    }

    private fun toMinimalForecast(index: Int, forecastDay: ForecastDay): MinimalForecast {
        return MinimalForecast(
            id = index,
            avgTempC = forecastDay.day.avgTempC,
            avgTempF = forecastDay.day.avgTempF,
            minTempC = forecastDay.day.minTempC,
            minTempF = forecastDay.day.minTempF,
            maxTempC = forecastDay.day.maxTempC,
            maxTempF = forecastDay.day.maxTempF,
            conditionIconUrl = forecastDay.day.condition.icon,
            date = forecastDay.dateEpoch,
        )
    }

    private fun toCurrent(current: Current): CurrentWeather {
        return CurrentWeather(
            lastUpdatedEpoch = current.lastUpdatedEpoch,
            tempC = current.tempC,
            tempF = current.tempF,
            isDay = current.isDay == 1,
            condition = toCondition(current.condition),
            windMph = current.windMph,
            windKph = current.gustKph,
            windDegree = current.windDegree,
            windDir = WindDirection.valueOf(current.windDir),
            pressureMb = current.pressureMb,
            pressureIn = current.pressureIn,
            precipMm = current.precipMm,
            precipIn = current.precipIn,
            humidity = current.humidity,
            cloud = current.cloud,
            feelsLikeC = current.feelslikeC,
            feelsLikeF = current.feelslikeF,
            visKm = current.visKm,
            visMiles = current.visMiles,
            uv = current.uv,
            gustMph = current.gustMph,
            gustKph = current.gustKph,
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
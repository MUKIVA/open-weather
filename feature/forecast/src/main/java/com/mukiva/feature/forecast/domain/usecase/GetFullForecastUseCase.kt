package com.mukiva.feature.forecast.domain.usecase

import com.github.mukiva.open_weather.core.domain.settings.UnitsType
import com.github.mukiva.open_weather.core.domain.weather.Distance
import com.github.mukiva.open_weather.core.domain.weather.Precipitation
import com.github.mukiva.open_weather.core.domain.weather.Pressure
import com.github.mukiva.open_weather.core.domain.weather.Speed
import com.github.mukiva.open_weather.core.domain.weather.Temp
import com.github.mukiva.open_weather.core.domain.weather.WindDirection
import com.github.mukiva.weather_data.ForecastRepository
import com.github.mukiva.weather_data.SettingsRepository
import com.github.mukiva.weather_data.models.Forecast
import com.github.mukiva.weather_data.models.Hour
import com.github.mukiva.weather_data.utils.RequestResult
import com.github.mukiva.weather_data.utils.map
import com.mukiva.feature.forecast.domain.ForecastItem
import com.mukiva.feature.forecast.presentation.HourlyForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFullForecastUseCase @Inject constructor(
    private val forecastRepo: ForecastRepository,
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(locationName: String): Flow<RequestResult<List<HourlyForecast.Content>>> {
        val request = forecastRepo.getForecast(locationName)
            .map { requestResult -> requestResult.map { it.forecast } }
        val unitsTypeFlow = settingsRepository.getUnitsType()
        return unitsTypeFlow.combine(request) { unitsType, requestResult ->
            requestResult.map { forecast -> toHourlyForecast(forecast, unitsType) }
        }
    }

    private fun toHourlyForecast(
        forecast: Forecast,
        unitsType: UnitsType
    ): List<HourlyForecast.Content> {
        return forecast.forecastDay.mapIndexed { index, forecastDay ->
            HourlyForecast.Content(
                index = index,
                date = forecastDay.dateEpoch,
                hours = forecastDay.hour.mapIndexed { id, hour ->
                    asForecastItem(id, hour, unitsType)
                }
            )
        }
    }

    private fun asForecastItem(
        index: Int,
        hour: Hour,
        unitsType: UnitsType,
    ): ForecastItem = with(hour) {
        return ForecastItem(
            id = index,
            humidity = humidity,
            pressure = Pressure(unitsType, pressureMb, pressureIn),
            precipitation = Precipitation(unitsType, precipMm, precipIn),
            temp = Temp(unitsType, tempC, tempF),
            feelsLike = Temp(unitsType, feelsLikeC, feelsLikeF),
            cloud = cloud,
            weatherIconUrl = condition.icon,
            dateTime = timeEpoch,
            windSpeed = Speed(unitsType, windKph, windMph),
            windDegree = windDegree,
            windDirection = WindDirection.valueOf(windDir),
            vis = Distance(unitsType, visKm, visMiles),
            gust = Speed(unitsType, gustKph, gustMph),
        )
    }
}

package com.github.mukiva.feature.forecast.domain.usecase

import com.github.mukiva.feature.forecast.domain.ForecastItem
import com.github.mukiva.feature.forecast.presentation.HourlyForecast
import com.github.mukiva.openweather.core.domain.settings.UnitsType
import com.github.mukiva.openweather.core.domain.weather.Distance
import com.github.mukiva.openweather.core.domain.weather.Precipitation
import com.github.mukiva.openweather.core.domain.weather.Pressure
import com.github.mukiva.openweather.core.domain.weather.Speed
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.openweather.core.domain.weather.WindDirection
import com.github.mukiva.weatherdata.IForecastRepository
import com.github.mukiva.weatherdata.ISettingsRepository
import com.github.mukiva.weatherdata.models.Forecast
import com.github.mukiva.weatherdata.models.Hour
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetFullForecastUseCase @Inject constructor(
    private val forecastRepo: IForecastRepository,
    private val settingsRepository: ISettingsRepository,
) {

    suspend operator fun invoke(
        locationId: Long,
        getCached: Boolean = false
    ): Flow<RequestResult<List<HourlyForecast>>> {
        val lang = settingsRepository
            .getLocalization()
            .first()
        val request = forecastRepo.getForecast(locationId, lang, getCached)
            .map { requestResult -> requestResult.map { it.forecast } }
        val unitsTypeFlow = settingsRepository.getUnitsType()

        return unitsTypeFlow.combine(request) { unitsType, requestResult ->
            requestResult.map { forecast -> toHourlyForecast(forecast, unitsType) }
        }
    }

    private fun toHourlyForecast(
        forecast: Forecast,
        unitsType: UnitsType
    ): List<HourlyForecast> {
        return forecast.forecastDay.mapIndexed { index, forecastDay ->
            HourlyForecast(
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
            weatherIconCode = condition.code,
            isDay = isDay == 1,
            dateTime = timeEpoch,
            windSpeed = Speed(unitsType, windKph, windMph),
            windDegree = windDegree,
            windDirection = WindDirection.valueOf(windDir),
            vis = Distance(unitsType, visKm, visMiles),
            gust = Speed(unitsType, gustKph, gustMph),
        )
    }
}

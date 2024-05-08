package com.mukiva.feature.forecast.domain.usecase

import com.github.mukiva.weather_data.ForecastRepository
import com.github.mukiva.weather_data.models.Forecast
import com.github.mukiva.weather_data.models.Hour
import com.github.mukiva.weather_data.utils.RequestResult
import com.github.mukiva.weather_data.utils.map
import com.mukiva.feature.forecast.domain.ForecastItem
import com.mukiva.feature.forecast.domain.WindDirection
import com.mukiva.feature.forecast.presentation.ForecastGroup
import com.mukiva.feature.forecast.presentation.HourlyForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFullForecastUseCase @Inject constructor(
    private val forecastRepo: ForecastRepository,
) {

    operator fun invoke(locationName: String): Flow<RequestResult<List<HourlyForecast>>> {
        return forecastRepo.getForecast(locationName)
            .map { requestResult -> requestResult.map { it.forecast } }
            .map { requestResult -> requestResult.map { forecast -> toHourlyForecast(forecast) } }
    }

    private fun toHourlyForecast(forecast: Forecast): List<HourlyForecast>  {
        return buildList {
            forecast.forecastDay.mapIndexed { index, forecastDay ->
                HourlyForecast(
                    index = index,
                    date = forecastDay.dateEpoch,
                    groups = createForecastGroups(forecastDay.hour) ,
                )
            }
        }
    }

    private fun createForecastGroups(hours: List<Hour>): List<ForecastGroup> {
        val tempGroup = ArrayList<ForecastItem>(HOURS_IN_DAY)
        val humidityGroup = ArrayList<ForecastItem>(HOURS_IN_DAY)
        val pressureGroup = ArrayList<ForecastItem>(HOURS_IN_DAY)
        val windGroup = ArrayList<ForecastItem>(HOURS_IN_DAY)

        hours.onEach { hour ->
            tempGroup.add(asHourlyTemp(hour))
            humidityGroup.add(asHourlyHumidity(hour))
            pressureGroup.add(asHourlyPressure(hour))
            windGroup.add(asHourlyWind(hour))
        }

        return listOf(
            asGroup(tempGroup, ForecastGroup.Type.TEMP),
            asGroup(windGroup, ForecastGroup.Type.WIND),
            asGroup(pressureGroup, ForecastGroup.Type.PRESSURE),
            asGroup(humidityGroup, ForecastGroup.Type.HUMIDITY)
        )
    }

    private fun asHourlyTemp(hour: Hour): ForecastItem.HourlyTemp {
        return ForecastItem.HourlyTemp(
            tempC = hour.tempC,
            tempF = hour.tempF,
            feelsLikeC = hour.feelsLikeC,
            feelsLikeF = hour.feelsLikeF,
            cloud = hour.cloud,
            iconUrl = hour.condition.icon,
            date = hour.timeEpoch,
        )
    }

    private fun asHourlyHumidity(hour: Hour): ForecastItem.HourlyHumidity {
        return ForecastItem.HourlyHumidity(
            humidity = hour.humidity,
            date = hour.timeEpoch,
        )
    }

    private fun asHourlyPressure(hour: Hour): ForecastItem.HourlyPressure {
        return ForecastItem.HourlyPressure(
            pressureMb = hour.pressureMb,
            pressureIn = hour.pressureIn,
            date = hour.timeEpoch,
        )
    }

    private fun asHourlyWind(hour: Hour): ForecastItem.HourlyWind {
        return ForecastItem.HourlyWind(
            windMph = hour.windMph,
            windKph = hour.windKph,
            windDegree = hour.windDegree,
            windDirection = WindDirection.valueOf(hour.windDir),
            date = hour.timeEpoch,
        )
    }

    private fun asGroup(
        forecastItems: List<ForecastItem>,
        forecastGroupType: ForecastGroup.Type,
    ): ForecastGroup {
        return ForecastGroup(
            forecastType = forecastGroupType,
            forecast = forecastItems
        )
    }

    companion object {
        private const val HOURS_IN_DAY = 24
    }

}
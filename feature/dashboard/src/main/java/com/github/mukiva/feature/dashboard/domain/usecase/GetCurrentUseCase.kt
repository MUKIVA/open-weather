package com.github.mukiva.feature.dashboard.domain.usecase

import com.github.mukiva.feature.dashboard.domain.model.Astro
import com.github.mukiva.feature.dashboard.domain.model.Current
import com.github.mukiva.feature.dashboard.domain.model.DayForecast
import com.github.mukiva.feature.dashboard.domain.model.Forecast
import com.github.mukiva.feature.dashboard.domain.model.Location
import com.github.mukiva.feature.dashboard.domain.model.Precipitation
import com.github.mukiva.openweather.core.domain.settings.UnitsType
import com.github.mukiva.openweather.core.domain.weather.Pressure
import com.github.mukiva.openweather.core.domain.weather.Speed
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.openweather.core.domain.weather.WindDirection
import com.github.mukiva.weatherdata.models.Current as DataCurrent
import com.github.mukiva.weatherdata.models.Day as DataDay
import com.github.mukiva.weatherdata.models.ForecastWithCurrentAndLocation
import com.github.mukiva.weatherdata.IForecastRepository
import com.github.mukiva.weatherdata.ISettingsRepository
import com.github.mukiva.weatherdata.models.ForecastDay
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import javax.inject.Inject
import com.github.mukiva.openweather.core.domain.weather.Precipitation as Precip
import com.github.mukiva.weatherdata.models.Astro as DataAstro
import com.github.mukiva.weatherdata.models.Location as LocationData

internal class GetCurrentUseCase @Inject constructor(
    private val forecastRepository: IForecastRepository,
    private val settingsRepository: ISettingsRepository
) {

    suspend operator fun invoke(locationId: Long): Flow<RequestResult<Forecast>> {
        val lang = settingsRepository.getLocalization()
            .first()
        val unitsType = settingsRepository.getUnitsType()
        val request = forecastRepository.getForecast(
            locationId = locationId,
            lang = lang
        )
        return request.combine(unitsType) { requestResult, units ->
            requestResult.map { dataForecast ->
                asDomainForecast(dataForecast, units)
            }
        }
    }

    private fun asDomainForecast(
        forecastData: ForecastWithCurrentAndLocation,
        unitsType: UnitsType
    ): Forecast {
        val currentData = forecastData.current
        val forecastDayData = forecastData.forecast.forecastDay.first()
        return Forecast(
            location = asDomainLocation(forecastData.location),
            current = asDomainCurrent(currentData, unitsType),
            precipitation = asDomainPrecipitation(forecastDayData.day, unitsType),
            astro = asDomainAstro(forecastDayData.astro),
            dayForecasts = forecastData.forecast.forecastDay.mapIndexed { index, forecastDay ->
                asDomainDayForecast(forecastDay, unitsType, index)
            },
        )
    }

    private fun asDomainCurrent(
        currentData: DataCurrent,
        unitsType: UnitsType
    ) = Current(
        isDay = currentData.isDay == 1,
        temp = Temp(unitsType, currentData.tempC,currentData.tempF),
        feelsLike = Temp(unitsType, currentData.feelslikeC,currentData.feelslikeF),
        cloudPercent = currentData.cloud,
        windSpeed = Speed(unitsType, currentData.windKph, currentData.windMph),
        windDirection = WindDirection.valueOf(currentData.windDir),
        humidityPercent = currentData.humidity,
        pressure = Pressure(unitsType, currentData.pressureMb, currentData.pressureIn),
        conditionImageCode = currentData.condition.code
    )

    private fun asDomainPrecipitation(
        dayData: DataDay,
        unitsType: UnitsType
    ) = Precipitation(
        willItRain = dayData.dailyWillItRain == 1,
        chanceOfRain = dayData.dailyChanceOfRain,
        willItSnow = dayData.dailyWillItSnow == 1,
        chanceOfSnow = dayData.dailyChanceOfSnow,
        precipitationAmount = Precip(unitsType, dayData.totalPrecipMm, dayData.totalPrecipIn),
    )

    private val dateTimeFormatter = LocalTime.Format {
        amPmHour(Padding.ZERO)
        char(':')
        minute(Padding.ZERO)
        char(' ')
        amPmMarker("AM", "PM")
    }

    private fun asDomainAstro(
        dayAstro: DataAstro
    ) = Astro(
        sunrise = LocalTime.parse(dayAstro.sunrise, dateTimeFormatter),
        sunset = LocalTime.parse(dayAstro.sunset, dateTimeFormatter),
        moonrise = LocalTime.parse(dayAstro.moonrise, dateTimeFormatter),
        moonset = LocalTime.parse(dayAstro.moonset, dateTimeFormatter),
    )

    private fun asDomainDayForecast(
        forecastDay: ForecastDay,
        unitsType: UnitsType,
        id: Int,
    ) = DayForecast(
        id = id,
        conditionIconCode = forecastDay.day.condition.code,
        date = forecastDay.dateEpoch,
        nightTemp = Temp(unitsType, forecastDay.day.minTempC, forecastDay.day.minTempF),
        dayTemp = Temp(unitsType, forecastDay.day.maxTempC, forecastDay.day.maxTempF),
    )

    private fun asDomainLocation(
        dataLocation: LocationData
    ) = Location(
        id = dataLocation.id,
        name = dataLocation.name,
        region = dataLocation.region,
        country = dataLocation.country,
        priority = dataLocation.priority
    )
}
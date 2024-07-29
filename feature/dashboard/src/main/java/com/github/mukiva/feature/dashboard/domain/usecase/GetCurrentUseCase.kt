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
import com.github.mukiva.weatherdata.models.CurrentData as DataCurrent
import com.github.mukiva.weatherdata.models.DayData as DataDay
import com.github.mukiva.weatherdata.models.ForecastWithCurrentAndLocationData
import com.github.mukiva.weatherdata.IForecastRepository
import com.github.mukiva.weatherdata.ISettingsRepository
import com.github.mukiva.weatherdata.models.ForecastDayData
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import javax.inject.Inject
import com.github.mukiva.openweather.core.domain.weather.Precipitation as Precip
import com.github.mukiva.weatherdata.models.AstroData as DataAstro
import com.github.mukiva.weatherdata.models.LocationData as LocationData

internal class GetCurrentUseCase @Inject constructor(
    private val forecastRepository: IForecastRepository,
    private val settingsRepository: ISettingsRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(locationId: Long): Flow<RequestResult<Forecast>> {
        val lang = settingsRepository.getLocalization()
            .flowOn(Dispatchers.Default)
        val unitsType = settingsRepository.getUnitsType()
            .flowOn(Dispatchers.Default)

        return combine(lang, unitsType) { language, units ->
            language to units
        }
            .flatMapLatest { (language, units) ->
                forecastRepository.getForecast(
                    locationId = locationId,
                    lang = language
                )
                    .flowOn(Dispatchers.Default)
                    .map { requestResult ->
                        requestResult.map { dataForecast ->
                            asDomainForecast(dataForecast, units)
                        }
                    }
            }

    }

    private fun asDomainForecast(
        forecastData: ForecastWithCurrentAndLocationData,
        unitsType: UnitsType
    ): Forecast {
        val currentData = forecastData.currentData
        val forecastDayData = forecastData.forecastData.forecastDayData.first()
        return Forecast(
            location = asDomainLocation(forecastData.locationData),
            current = asDomainCurrent(currentData, unitsType),
            precipitation = asDomainPrecipitation(forecastDayData.dayData, unitsType),
            astro = asDomainAstro(forecastDayData.astroData),
            dayForecasts = forecastData.forecastData.forecastDayData.mapIndexed { index, forecastDay ->
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
        conditionImageCode = currentData.conditionData.code
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
        forecastDayData: ForecastDayData,
        unitsType: UnitsType,
        id: Int,
    ) = DayForecast(
        id = id,
        conditionIconCode = forecastDayData.dayData.conditionData.code,
        date = forecastDayData.dateEpoch,
        nightTemp = Temp(unitsType, forecastDayData.dayData.minTempC, forecastDayData.dayData.minTempF),
        dayTemp = Temp(unitsType, forecastDayData.dayData.maxTempC, forecastDayData.dayData.maxTempF),
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
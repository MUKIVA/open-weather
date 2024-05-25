package com.github.mukiva.feature.dashboard.domain.usecase

import com.github.mukiva.feature.dashboard.domain.model.ForecastDataWrapper
import com.github.mukiva.weatherdata.ForecastRepository
import com.github.mukiva.weatherdata.LocationRepository
import com.github.mukiva.weatherdata.SettingsRepository
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository
) {

    suspend operator fun invoke(): Flow<RequestResult<ForecastDataWrapper>> {
        val lang = settingsRepository.getLocalization()
            .first()
        val unitsType = settingsRepository.getUnitsType()
            .first()
        val locations = locationRepository.getAllLocal()
            .filter { requestResult -> requestResult !is RequestResult.InProgress }
            .first()
        if (locations is RequestResult.Error) {
            return flow {
                RequestResult.Error(
                    ForecastDataWrapper(
                        errorType = ForecastDataWrapper.ErrorType.GET_LOCATION_ERROR,
                        unitsType = unitsType,
                    )
                )
            }
        }
        val locationsData = locations.data
        if (locationsData.isNullOrEmpty()) {
            return flow {
                RequestResult.Error(
                    ForecastDataWrapper(
                        errorType = ForecastDataWrapper.ErrorType.LOCATION_NOT_FOUND,
                        unitsType = unitsType
                    )
                )
            }
        }
        val location = locationsData.first()
        return forecastRepository.getForecast(location.id, lang)
            .map { requestResult ->
                requestResult.map { data ->
                    ForecastDataWrapper(
                        unitsType = unitsType,
                        errorType = ForecastDataWrapper.ErrorType.NOTHING,
                        data = data,
                    )
                }
            }
    }
}

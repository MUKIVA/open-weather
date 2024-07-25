package com.github.mukiva.feature.dashboard.domain.usecase

import com.github.mukiva.feature.dashboard.domain.model.ForecastDataWrapper
import com.github.mukiva.weatherdata.IForecastRepository
import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.ISettingsRepository
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetCurrentWeatherUseCase @Inject constructor(
    private val settingsRepository: ISettingsRepository,
    private val locationRepository: ILocationRepository,
    private val forecastRepository: IForecastRepository
) {

    suspend operator fun invoke(): Flow<RequestResult<ForecastDataWrapper>> {
        val lang = settingsRepository.getLocalization()
            .flowOn(Dispatchers.Default)
            .first()
        val unitsType = settingsRepository.getUnitsType()
            .flowOn(Dispatchers.Default)
            .first()
        val locations = locationRepository.getAllLocal()
            .flowOn(Dispatchers.Default)
            .filter { requestResult -> requestResult !is RequestResult.InProgress }
            .first()
        if (locations is RequestResult.Error) {
            return flow {
                RequestResult.Error(
                    ForecastDataWrapper(
                        errorType = ForecastDataWrapper.ErrorType.GET_LOCATION_ERROR,
                        unitsType = unitsType,
                    ),
                    cause = locations.cause
                )
            }
        }
        val locationsData = (locations as? RequestResult.Success)
            ?.data
        if (locationsData.isNullOrEmpty()) {
            return flow {
                RequestResult.Error(
                    ForecastDataWrapper(
                        errorType = ForecastDataWrapper.ErrorType.LOCATION_NOT_FOUND,
                        unitsType = unitsType
                    ),
                    cause = null
                )
            }
        }
        val location = locationsData.first()
        return forecastRepository.getForecast(location.id, lang)
            .flowOn(Dispatchers.Default)
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

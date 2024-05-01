package com.mukiva.feature.forecast.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.feature.forecast.domain.UnitsType
import com.mukiva.feature.forecast.domain.repository.ISettingsRepository
import com.mukiva.feature.forecast.domain.usecase.GetFullForecastUseCase
import com.mukiva.openweather.presentation.SingleStateViewModel
import com.mukiva.usecase.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    initialState: ForecastState,
//    forecastUpdater: IForecastUpdater,
    settings: ISettingsRepository,
    private val getFullForecastUseCase: GetFullForecastUseCase,
) : SingleStateViewModel<ForecastState, Nothing>(initialState) {

    private var mUnitsType = UnitsType.METRIC

    init {
        viewModelScope.launch {
            settings.getUnitsTypeFlow()
                .onEach { mUnitsType = it }
                .launchIn(viewModelScope)
        }
    }

    fun loadForecast(locationName: String) {
        modifyState { ForecastState.Loading }

        viewModelScope.launch {
            when (val result = getFullForecastUseCase(locationName, FORECAST_DAY_COUNT)) {
                is ApiResult.Error -> modifyState { ForecastState.Error }
                is ApiResult.Success -> modifyState {
                    ForecastState.Content(
                        unitsType = mUnitsType,
                        hourlyForecast = result.data
                    )
                }
            }
        }

    }

    companion object {
        private const val FORECAST_DAY_COUNT = 3
    }

}
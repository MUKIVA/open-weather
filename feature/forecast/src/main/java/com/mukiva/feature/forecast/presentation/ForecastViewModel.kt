package com.mukiva.feature.forecast.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.feature.forecast.domain.usecase.GetFullForecastUseCase
import com.mukiva.openweather.presentation.SingleStateViewModel
import com.mukiva.usecase.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    initialState: ForecastState,
    private val getFullForecastUseCase: GetFullForecastUseCase
) : SingleStateViewModel<ForecastState, Nothing>(initialState) {

    fun loadForecast(locationName: String) {
        modifyState { copy(type = ForecastState.Type.LOADING) }

        viewModelScope.launch {
            when (val result = getFullForecastUseCase(locationName, FORECAST_DAY_COUNT)) {
                is ApiResult.Error -> modifyState { copy(type = ForecastState.Type.ERROR) }
                is ApiResult.Success -> modifyState {
                    copy(
                        type = ForecastState.Type.CONTENT,
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
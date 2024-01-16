package com.mukiva.feature.forecast.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.feature.forecast.domain.usecase.GetForecastUseCase
import com.mukiva.openweather.presentation.SingleStateViewModel
import com.mukiva.usecase.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastMinimalViewModel @Inject constructor(
    initialState: MinimalForecastState,
    private val getForecastUseCase: GetForecastUseCase,
) : SingleStateViewModel<MinimalForecastState, Nothing>(initialState) {

    fun loadForecast(location: String) {
        viewModelScope.launch {
            when(val result = getForecastUseCase(location)) {
                is ApiResult.Error -> modifyState { copy(type = MinimalForecastState.Type.ERROR) }
                is ApiResult.Success -> modifyState {
                    copy(
                        type = MinimalForecastState.Type.CONTENT,
                        forecastItems = result.data
                    )
                }
            }

        }
    }

    fun onItemClick(position: Int) {
        TODO("Implement full days forecast page")
    }

}
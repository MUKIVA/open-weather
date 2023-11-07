package com.mukiva.current_weather.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.current_weather.domain.usecase.GetCurrentWeatherUseCase
import com.mukiva.openweather.presentation.SingleStateViewModel
import com.mukiva.usecase.ApiResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrentWeatherViewModel @Inject constructor(
    initialState: CurrentWeatherState,
    val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : SingleStateViewModel<CurrentWeatherState, Nothing>(initialState) {

    fun fetchWeather() {
        viewModelScope.launch {
            when(val result = getCurrentWeatherUseCase()) {
                is ApiResult.Error -> {
                    modifyState {
                        copy(title = "FETCH ERROR: ${result.err}")
                    }
                }
                is ApiResult.Success -> {
                    modifyState {
                        copy(title = "FETCH SUCCESS: ${result.data}")
                    }
                }
            }

        }
    }

}
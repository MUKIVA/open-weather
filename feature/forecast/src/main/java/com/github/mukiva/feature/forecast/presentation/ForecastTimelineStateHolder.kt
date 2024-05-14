package com.github.mukiva.feature.forecast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.takeWhile

class ForecastTimelineStateHolder(
    private val mainViewModel: ForecastViewModel
) : ViewModel() {

    val state: StateFlow<HourlyForecast>
        get() = mState.asStateFlow()

    private val mState = MutableStateFlow<HourlyForecast>(HourlyForecast.Init)

    fun loadHours(position: Int) {
        mainViewModel.state
            .takeWhile { state ->
                if (state is ForecastState.Content) {
                    mState.emit(state.hourlyForecast[position])
                }
                state is ForecastState.Init || state is ForecastState.Loading
            }
            .launchIn(viewModelScope)
    }
}

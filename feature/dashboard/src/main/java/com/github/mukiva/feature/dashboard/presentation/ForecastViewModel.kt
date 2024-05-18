package com.github.mukiva.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.dashboard.domain.model.Forecast
import com.github.mukiva.feature.dashboard.domain.usecase.GetForecastUseCase
import com.github.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.github.mukiva.weatherdata.utils.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
    private val statesHolder: ForecastStatesHolder,
    router: IDashboardRouter
) : ViewModel(), IDashboardRouter by router {
    fun state(id: Long): StateFlow<LocationWeatherState> = statesHolder[id]

    fun loadForecast(id: Long) {
        getForecastUseCase(id)
            .map(::asState)
            .onEach { statesHolder[id].emit(it) }
            .launchIn(viewModelScope)
    }

    private fun asState(requestResult: RequestResult<Forecast>): LocationWeatherState {
        return when (requestResult) {
            is RequestResult.Error -> LocationWeatherState.Error
            is RequestResult.InProgress -> LocationWeatherState.Loading
            is RequestResult.Success -> {
                LocationWeatherState.Content(
                    forecast = checkNotNull(requestResult.data),
                )
            }
        }
    }
}

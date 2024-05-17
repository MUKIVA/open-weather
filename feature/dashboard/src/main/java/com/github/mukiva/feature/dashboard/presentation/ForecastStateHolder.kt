package com.github.mukiva.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.dashboard.domain.model.Forecast
import com.github.mukiva.feature.dashboard.domain.usecase.GetForecastUseCase
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ForecastStateHolder(
    getForecastUseCase: GetForecastUseCase,
    locationName: String
) : ViewModel(), IForecastStateHolder {

    override val forecastState: StateFlow<LocationWeatherState>
        get() = mState

    private val mState = getForecastUseCase(locationName)
        .map { requestResult -> asState(requestResult) }
        .stateIn(viewModelScope, SharingStarted.Lazily, LocationWeatherState.Init)

    private fun asState(requestResult: RequestResult<Forecast>): LocationWeatherState {
        return when (requestResult) {
            is RequestResult.Error -> LocationWeatherState.Error
            is RequestResult.InProgress -> LocationWeatherState.Loading
            is RequestResult.Success -> LocationWeatherState.Content(
                forecast = checkNotNull(requestResult.data),
            )
        }
    }
}

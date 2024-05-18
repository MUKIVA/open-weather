package com.github.mukiva.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.dashboard.domain.model.Forecast
import com.github.mukiva.feature.dashboard.domain.usecase.GetForecastUseCase
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ForecastStateHolder @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
) : ViewModel(), IForecastStateHolder {

    override val forecastState: StateFlow<LocationWeatherState>
        get() = mState.asStateFlow()

    private val mState = MutableStateFlow<LocationWeatherState>(LocationWeatherState.Init)
    override fun loadForecast(name: String) {
        getForecastUseCase(name)
            .map { requestResult -> asState(requestResult) }
            .onEach(mState::emit)
            .launchIn(viewModelScope)
    }

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

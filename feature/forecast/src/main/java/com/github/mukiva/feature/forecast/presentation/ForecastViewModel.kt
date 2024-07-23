package com.github.mukiva.feature.forecast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.forecast.domain.usecase.GetFullForecastUseCase
import com.github.mukiva.weatherdata.utils.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ForecastViewModel @Inject constructor(
    private val getFullForecastUseCase: GetFullForecastUseCase,
) : ViewModel() {

    val state: StateFlow<ForecastState>
        get() = mState

    private val mState = MutableStateFlow<ForecastState>(ForecastState.Init)
    fun loadForecast(id: Long, cached: Boolean) {
        viewModelScope.launch {
            getFullForecastUseCase(id, cached)
                .map(::asState)
                .onEach(mState::emit)
                .filter { state -> state !is ForecastState.Loading && state !is ForecastState.Init }
                .first()
        }
    }

    private fun asState(
        requestResult: RequestResult<List<HourlyForecast>>
    ): ForecastState {
        return when (requestResult) {
            is RequestResult.Error -> ForecastState.Error
            is RequestResult.InProgress -> ForecastState.Loading
            is RequestResult.Success -> ForecastState.Content(
                hourlyForecast = checkNotNull(requestResult.data),
            )
        }
    }
}

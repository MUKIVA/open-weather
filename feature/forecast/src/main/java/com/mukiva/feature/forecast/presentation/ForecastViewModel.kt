package com.mukiva.feature.forecast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.weather_data.utils.RequestResult
import com.mukiva.feature.forecast.domain.usecase.GetFullForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.takeWhile
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getFullForecastUseCase: GetFullForecastUseCase,
) : ViewModel() {

    val state: StateFlow<ForecastState>
        get() = mState.asStateFlow()

    private val mState =
        MutableStateFlow<ForecastState>(ForecastState.Init)

    private var mTimelineStateHolders = emptyList<ForecastTimelineStateHolder>()
    fun loadForecast(locationName: String) {
        getFullForecastUseCase(locationName)
            .takeWhile { requestResult ->
                if (requestResult is RequestResult.Success)
                    updateStateHolders(checkNotNull(requestResult.data).size)
                mState.emit(asState(requestResult))
                requestResult is RequestResult.InProgress
            }
            .launchIn(viewModelScope)
    }
    fun getStateHolder(position: Int): ForecastTimelineStateHolder {
        return mTimelineStateHolders[position]
    }
    private fun updateStateHolders(size: Int) {
        mTimelineStateHolders = buildList(size) {
            repeat(size) { add(ForecastTimelineStateHolder(this@ForecastViewModel)) }
        }
    }

    private fun asState(
        requestResult: RequestResult<List<HourlyForecast.Content>>
    ): ForecastState {
        return when(requestResult) {
            is RequestResult.Error -> ForecastState.Error
            is RequestResult.InProgress -> ForecastState.Loading
            is RequestResult.Success -> ForecastState.Content(
                hourlyForecast = checkNotNull(requestResult.data),
            )
        }
    }

}
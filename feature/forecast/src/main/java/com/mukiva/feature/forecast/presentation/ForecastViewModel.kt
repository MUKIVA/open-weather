package com.mukiva.feature.forecast.presentation

import com.mukiva.openweather.presentation.SingleStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    initialState: ForecastState
) : SingleStateViewModel<ForecastState, Nothing>(initialState) {

    fun loadForecast() {
        modifyState { copy(type = ForecastState.Type.LOADING) }
    }

}
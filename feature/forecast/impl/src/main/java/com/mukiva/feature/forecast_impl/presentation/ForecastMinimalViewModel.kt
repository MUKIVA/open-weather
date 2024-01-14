package com.mukiva.feature.forecast_impl.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.core.navigation.INavigator
import com.mukiva.feature.forecast_impl.domain.usecase.GetForecastUseCase
import com.mukiva.openweather.presentation.SingleStateViewModel
import com.mukiva.usecase.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastMinimalViewModel @Inject constructor(
    initialState: MinimalForecastState,
    private val getForecastUseCase: GetForecastUseCase,
    private val navigator: INavigator
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
    }

}
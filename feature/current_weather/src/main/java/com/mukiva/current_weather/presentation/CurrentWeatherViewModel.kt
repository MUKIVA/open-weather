package com.mukiva.current_weather.presentation

import com.mukiva.core.navigation.INavigator
import com.mukiva.current_weather.navigation.SettingsScreen
import com.mukiva.openweather.presentation.SingleStateViewModel
import javax.inject.Inject

class CurrentWeatherViewModel @Inject constructor(
    initialState: CurrentWeatherState,
//    val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val navigator: INavigator
) : SingleStateViewModel<CurrentWeatherState, Nothing>(initialState) {

    fun onSelectLocations() {
        navigator.launch(SettingsScreen())
    }

//    fun fetchWeather() {
//        viewModelScope.launch {
//            when(val result = getCurrentWeatherUseCase()) {
//                is ApiResult.Error -> {
//                    modifyState {
//                        copy(title = "FETCH ERROR: ${result.err}")
//                    }
//                }
//                is ApiResult.Success -> {
//                    modifyState {
//                        copy(title = "FETCH SUCCESS: ${result.data}")
//                    }
//                }
//            }
//
//        }
//    }

}
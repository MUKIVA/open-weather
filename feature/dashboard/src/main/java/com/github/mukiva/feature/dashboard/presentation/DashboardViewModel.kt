package com.github.mukiva.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.dashboard.domain.model.Location
import com.github.mukiva.feature.dashboard.domain.usecase.GetAllLocationsUseCase
import com.github.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.github.mukiva.weatherdata.utils.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllLocationsUseCase: GetAllLocationsUseCase,
    private val weatherStatesHolder: ForecastStatesHolder,
    router: IDashboardRouter
) : ViewModel(), IDashboardRouter by router {

    val state: StateFlow<DashboardState> = getAllLocationsUseCase()
        .map(::asState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            DashboardState.Init
        ).apply {
            onEach { state ->
                if (state is DashboardState.Content) {
                    weatherStatesHolder.clear()
                }
            }
        }

    fun loadLocations() { getAllLocationsUseCase() }

    fun requestWeatherState(id: Long): Flow<MainCardState> {
        return weatherStatesHolder[id].map(::asMainCardState)
    }

    private fun asState(requestResult: RequestResult<List<Location>>): DashboardState {
        return when (requestResult) {
            is RequestResult.Error -> DashboardState.Error
            is RequestResult.InProgress -> DashboardState.Loading
            is RequestResult.Success -> stateContentFactory(checkNotNull(requestResult.data))
        }
    }

    private fun asMainCardState(
        locationWeatherState: LocationWeatherState
    ): MainCardState {
        return when (locationWeatherState) {
            is LocationWeatherState.Content -> MainCardState.Content(
                locationName = locationWeatherState.forecast.locationName,
                currentTemp = locationWeatherState.forecast.currentWeather.temp,
            )
            else -> MainCardState.Loading
        }
    }

    private fun stateContentFactory(locations: List<Location>): DashboardState {
        return when {
            locations.isEmpty() -> DashboardState.Empty
            else -> DashboardState.Content(locations = locations)
        }
    }
}

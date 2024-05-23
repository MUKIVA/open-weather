package com.github.mukiva.feature.dashboard.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.dashboard.domain.model.Location
import com.github.mukiva.feature.dashboard.domain.usecase.GetAllLocationsUseCase
import com.github.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.github.mukiva.weatherdata.utils.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllLocationsUseCase: GetAllLocationsUseCase,
    private val weatherStatesHolder: ForecastStatesHolder,
    router: IDashboardRouter
) : ViewModel(), IDashboardRouter by router {
    val state: StateFlow<DashboardState>
        get() = mState

    private val mState = MutableStateFlow<DashboardState>(DashboardState.Init)

    init {
        getAllLocationsUseCase()
            .filter { mState.value !is DashboardState.Init }
            .onEach { loadLocations() }
            .launchIn(viewModelScope)
    }

    fun loadLocations() {
        viewModelScope.launch {
            getAllLocationsUseCase()
                .map(::asState)
                .onEach(mState::emit)
                .onEach { Log.d("STATE", "$it") }
                .filter { state -> state !is DashboardState.Loading && state !is DashboardState.Init }
                .onEach { weatherStatesHolder.clear() }
                .first()
        }
    }

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

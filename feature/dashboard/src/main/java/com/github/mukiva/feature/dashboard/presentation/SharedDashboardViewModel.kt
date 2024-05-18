package com.github.mukiva.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.dashboard.domain.usecase.GetForecastUseCase
import com.github.mukiva.feature.dashboard.navigation.IDashboardRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SharedDashboardViewModel @Inject constructor(
    dashboardViewModel: IDashboardViewModel,
    private val getForecastUseCase: GetForecastUseCase,
    router: IDashboardRouter
) : ViewModel(),
    IDashboardViewModel by dashboardViewModel,
    IDashboardRouter by router {
    val mainCardState: StateFlow<MainCardState>
        get() = mMainCardState.asStateFlow()

    private val mMainCardState = MutableStateFlow<MainCardState>(MainCardState.Loading)
    private val mForecastStateHolders = HashMap<String, ForecastStateHolder>()

    fun requestMainCardState(position: Int) {
        locationListState
            .filterIsInstance<DashboardState.Content>()
            .map { dashboardState -> dashboardState.locations[position].name }
            .map { locationName -> locationName to getForecastStateHolder(locationName) }
            .map { (name, holder) -> name to holder.forecastState }
            .onEach { (name, flow) -> requestMainCardState(name, flow) }
            .launchIn(viewModelScope)
    }

    fun getForecastStateHolder(name: String): ForecastStateHolder {
        val holder = mForecastStateHolders[name] ?: ForecastStateHolder(getForecastUseCase).apply {
            mForecastStateHolders[name] = this
        }
        return holder
    }

    private fun requestMainCardState(locationName: String, holderFlow: Flow<LocationWeatherState>) {
        holderFlow
            .onEach { state -> mMainCardState.emit(asMainCardState(state, locationName)) }
            .launchIn(viewModelScope)
    }

    private fun asMainCardState(
        locationWeatherState: LocationWeatherState,
        locationName: String,
    ): MainCardState {
        return when (locationWeatherState) {
            is LocationWeatherState.Content -> MainCardState.Content(
                locationName = locationName,
                currentTemp = locationWeatherState.forecast.currentWeather.temp
            )
            else -> MainCardState.Loading
        }
    }
}

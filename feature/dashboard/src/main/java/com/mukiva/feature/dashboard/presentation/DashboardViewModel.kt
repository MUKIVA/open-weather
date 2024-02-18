package com.mukiva.feature.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.mukiva.feature.dashboard.domain.model.CurrentWithLocation
import com.mukiva.feature.dashboard.domain.model.Location
import com.mukiva.feature.dashboard.domain.model.UnitsType
import com.mukiva.feature.dashboard.domain.repository.IForecastUpdater
import com.mukiva.feature.dashboard.domain.repository.ISettingsRepository
import com.mukiva.feature.dashboard.domain.usecase.GetAllLocationsUseCase
import com.mukiva.feature.dashboard.domain.usecase.GetCurrentWeatherUseCase
import com.mukiva.openweather.presentation.SingleStateViewModel
import com.mukiva.usecase.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    initialState: DashboardState,
    private val getAllLocationsUseCase: GetAllLocationsUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val router: IDashboardRouter,
    private val settingsRepository: ISettingsRepository,
    private val dataSynchronizer: DataSynchronizer,
    forecastUpdater: IForecastUpdater
) : SingleStateViewModel<DashboardState, Nothing>(initialState) {

    val position get() = mPosition
    var toolbarIsCollapsed: Boolean = false

    private var mPosition: Int = 0

    init {
        viewModelScope.launch {
            settingsRepository.getUnitsTypeFlow()
                .collect { onUnitsTypeUpdate(it) }
        }

        forecastUpdater.observeUpdate { load() }

    }

    fun load() {
        modifyState { copy(type = DashboardState.Type.LOADING) }

        viewModelScope.launch {
            val locations = getAllLocations()
            val currentWithLocationList = locations.map {
                CurrentWithLocation(null, emptyList(), it)
            }

            if (locations.isEmpty()){
                modifyState { copy(type = DashboardState.Type.EMPTY) }
                return@launch
            }

            modifyState {
                copy(
                    locationCount = locations.size
                )
            }

            dataSynchronizer.submit(currentWithLocationList)
            onPageSelect(mPosition)
        }
    }

    fun onSelectLocations() {
        router.goLocationManager()
    }

    fun onPageSelect(position: Int) {

        val locations = dataSynchronizer.lastData
        val location = locations[position].location
        mPosition = position

        viewModelScope.launch {
            when (val result = getCurrentWeatherUseCase(location.name)) {
                is ApiResult.Error -> modifyState { copy(type = DashboardState.Type.ERROR) }
                is ApiResult.Success -> {
                    val newLocationList = locations.mapIndexed { i, it ->
                        if (i == position)
                            it.copy(
                                currentWeather = result.data.currentWeather,
                                forecastState = result.data.forecastState
                            )
                        else
                            it
                    }
                    modifyState {
                        copy(
                            type = DashboardState.Type.CONTENT,
                            currentWeather = result.data
                        )
                    }
                    dataSynchronizer.submit(newLocationList)
                }
            }
        }
    }

    fun onSettings() {
        router.goSettings()
    }

    private suspend fun getAllLocations(): List<Location> {
        return when (val result = getAllLocationsUseCase()) {
                is ApiResult.Error -> emptyList()
                is ApiResult.Success -> result.data.sortedBy { it.position }
        }
    }

    private fun onUnitsTypeUpdate(unitsType: UnitsType) {
        modifyState {
            copy(
                unitsType = unitsType
            )
        }
    }

}
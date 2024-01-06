package com.presentation

import androidx.lifecycle.viewModelScope
import com.domain.model.CurrentWithLocation
import com.domain.model.Location
import com.domain.usecase.GetAllLocationsUseCase
import com.domain.usecase.GetCurrentWeatherUseCase
import com.mukiva.api.domain.AppConfig
import com.mukiva.api.navigation.ISettingsScreenProvider
import com.mukiva.api.repository.ISettingsRepository
import com.mukiva.core.navigation.INavigator
import com.mukiva.feature.location_manager_api.navigation.ILocationManagerScreenProvider
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
    private val navigator: INavigator,
    private val settingsScreenProvider: ISettingsScreenProvider,
    private val locationManagerScreenProvider: ILocationManagerScreenProvider,
    private val settingsRepository: ISettingsRepository,
    private val dataSynchronizer: DataSynchronizer
) : SingleStateViewModel<DashboardState, Nothing>(initialState) {

    init {
        viewModelScope.launch {
            settingsRepository.asAppConfig()
                .collect { onAppConfigUpdate(it) }
        }
    }

    fun load() {
        modifyState { copy(type = DashboardState.Type.LOADING) }

        viewModelScope.launch {
            val locations = getAllLocations()
            val currentWithLocationList = locations.map {
                CurrentWithLocation(null, it)
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
            onPageSelect(state.value.currentIndex)
        }
    }

    fun onSelectLocations() {
        navigator.launch(locationManagerScreenProvider.screen)
    }

    fun onPageSelect(position: Int) {

        val locations = dataSynchronizer.lastData
        val location = locations[position].location

        viewModelScope.launch {
            when (val result = getCurrentWeatherUseCase(location.name)) {
                is ApiResult.Error -> modifyState { copy(type = DashboardState.Type.ERROR) }
                is ApiResult.Success -> {
                    val newLocationList = locations.mapIndexed { i, it ->
                        if (i == position) it.copy(currentWeather = result.data.currentWeather) else it
                    }
                    modifyState {
                        copy(
                            type = DashboardState.Type.CONTENT,
                            currentWeather = result.data,
                            currentIndex = position
                        )
                    }
                    dataSynchronizer.submit(newLocationList)
                }
            }
        }
    }

    fun onSettings() {
        navigator.launch(settingsScreenProvider.screen)
    }

    private suspend fun getAllLocations(): List<Location> {
        return when (val result = getAllLocationsUseCase()) {
                is ApiResult.Error -> emptyList()
                is ApiResult.Success -> result.data
        }
    }

    private fun onAppConfigUpdate(cfg: AppConfig) {
        modifyState {
            copy(
                tempUnitsType = cfg.tempUnits
            )
        }
    }

}
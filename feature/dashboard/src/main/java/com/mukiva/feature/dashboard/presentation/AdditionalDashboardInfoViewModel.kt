package com.mukiva.feature.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.feature.dashboard.domain.model.CurrentWithLocation
import com.mukiva.feature.dashboard.domain.repository.ISettingsRepository
import com.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.mukiva.openweather.presentation.SingleStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AdditionalDashboardInfoViewModel @Inject constructor(
    initialState: AdditionalInfoState,
    private val settingsRepository: ISettingsRepository,
    private val dataSynchronizer: DataSynchronizer,
    private val router: IDashboardRouter
) : SingleStateViewModel<AdditionalInfoState, Nothing>(initialState) {

    private var mCachedLocationName: String = ""

    init {
        viewModelScope.launch {
            settingsRepository.getUnitsTypeFlow()
                .collect {
                    modifyState { copy(
                        unitsType = it
                    ) }
                    updateState(dataSynchronizer.lastData)
                }
        }
    }

    fun loadPosition(pos: Int) {
        modifyState { copy(position = pos) }
        dataSynchronizer.addListener {
            updateState(it)
        }
    }

    fun goForecast(pos: Int) {
        router.goFullForecast(mCachedLocationName, pos)
    }

    private fun updateState(locations: List<CurrentWithLocation>) {
        val currentWeather = locations[state.value.position].currentWeather
        val currentLocation = locations[state.value.position].location
        val forecast = locations[state.value.position].forecastState
        mCachedLocationName = currentLocation.name
        modifyState {
            copy(
                type = if (currentWeather == null)
                    AdditionalInfoState.Type.LOADING
                else
                    AdditionalInfoState.Type.CONTENT,
                currentWeather = currentWeather,
                forecastListState = forecast
            )
        }
    }
}
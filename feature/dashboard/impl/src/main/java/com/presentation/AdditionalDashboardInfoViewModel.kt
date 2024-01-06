package com.presentation

import androidx.lifecycle.viewModelScope
import com.domain.model.CurrentWithLocation
import com.mukiva.api.repository.ISettingsRepository
import com.mukiva.openweather.presentation.SingleStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AdditionalDashboardInfoViewModel @Inject constructor(
    initialState: AdditionalInfoState,
    private val settingsRepository: ISettingsRepository,
    private val dataSynchronizer: DataSynchronizer
) : SingleStateViewModel<AdditionalInfoState, Nothing>(initialState) {


    init {
        viewModelScope.launch {
            settingsRepository.asAppConfig()
                .collect {
                    modifyState { copy(
                        tempUnitsType = it.tempUnits,
                        speedUnitsType = it.speedUnits
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

    private fun updateState(locations: List<CurrentWithLocation>) {
        val currentWeather = locations[state.value.position].currentWeather
        modifyState {
            copy(
                type = if (currentWeather == null)
                    AdditionalInfoState.Type.LOADING
                else
                    AdditionalInfoState.Type.CONTENT,
                currentWeather = currentWeather
            )
        }
    }

}
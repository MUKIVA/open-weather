package com.mukiva.feature.dashboard_impl.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.feature.dashboard_impl.domain.model.CurrentWithLocation
import com.mukiva.feature.settings_api.repository.ISettingsRepository
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
                        unitsType = it.unitsType
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
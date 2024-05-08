package com.mukiva.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.weather_data.utils.RequestResult
import com.mukiva.feature.dashboard.domain.model.Forecast
import com.mukiva.feature.dashboard.domain.model.UnitsType
import com.mukiva.feature.dashboard.domain.repository.ISettingsRepository
import com.mukiva.feature.dashboard.domain.usecase.GetForecastUseCase
import com.mukiva.feature.dashboard.navigation.IDashboardRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
    private val router: IDashboardRouter,
    settingsRepository: ISettingsRepository,
) : ViewModel() {

    val state: StateFlow<LocationWeatherState>
        get() = mState.asStateFlow()

    private val mState = MutableStateFlow<LocationWeatherState>(LocationWeatherState.Init)

    private val mUnitsTypeState = settingsRepository.getUnitsTypeFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, UnitsType.METRIC)

    fun loadForecast(name: String) {
        getForecastUseCase(name)
            .combine(mUnitsTypeState, ::combineRequestWithUnitsType)
            .map { (requestResult, units) -> asState(requestResult, units)}
            .onEach(mState::emit)
            .launchIn(viewModelScope)
    }

    fun goForecast(locationName: String, position: Int) {
        router.goFullForecast(locationName, position)
    }

    private fun combineRequestWithUnitsType(
        requestResult: RequestResult<Forecast>,
        units: UnitsType
    ) = Pair(requestResult, units)

    private fun asState(requestResult: RequestResult<Forecast>, units: UnitsType): LocationWeatherState {
        return when(requestResult) {
            is RequestResult.Error -> LocationWeatherState.Error
            is RequestResult.InProgress -> LocationWeatherState.Loading
            is RequestResult.Success -> LocationWeatherState.Content(
                forecast = checkNotNull(requestResult.data),
                unitsType = units,
            )
        }
    }
}
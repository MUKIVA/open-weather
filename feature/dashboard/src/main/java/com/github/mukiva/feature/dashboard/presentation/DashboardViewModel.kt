package com.github.mukiva.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.dashboard.domain.model.Location
import com.github.mukiva.feature.dashboard.domain.usecase.GetAllLocationsUseCase
import com.github.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.github.mukiva.weatherdata.utils.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    private val getAllLocationsUseCase: GetAllLocationsUseCase,
    private val router: IDashboardRouter,
    private val forecastLoader: IForecastLoader
)
    : ViewModel()
    , IForecastLoader by forecastLoader
    , IDashboardRouter by router
{

    val state: StateFlow<IDashboardState>
        get() = mState.asStateFlow()

    private val mState = MutableStateFlow<IDashboardState>(IDashboardState.Loading)
    private var mLoadLocationsJob: Job = getAllLocationsUseCase()
        .map(::asState)
        .onEach(mState::emit)
        .launchIn(viewModelScope)
    private var mActionBarObserverJob: Job? = null

    fun requestLoad() {
        forecastLoader.cleanLoadedData()
        mLoadLocationsJob.cancel()
        mLoadLocationsJob = getAllLocationsUseCase()
            .map(::asState)
            .onEach(mState::emit)
            .launchIn(viewModelScope)
    }

    suspend fun selectPage(index: Int) {
        val stateInstance = mState.value as? IDashboardState.Content
            ?: return
        val location = stateInstance.forecasts[index]
        mActionBarObserverJob?.cancel()
        mActionBarObserverJob = provideForecastState(location.id)
            .onEach { state -> updateActionBarState(state, location) }
            .launchIn(viewModelScope)
    }

    private fun updateActionBarState(
        currentState: ICurrentState,
        location: Location
    ) {
        val newState = asActionBarState(currentState, location)
        mState.update { state ->
            val instance = state as? IDashboardState.Content
                ?: return@update state
            instance.copy(actionBarState = newState)
        }
    }

    private fun asActionBarState(
        state: ICurrentState,
        location: Location
    ): IActionBarState {
        return when (state) {
            is ICurrentState.Content -> IActionBarState.Content(
                location = location.name,
                imageCode = state.currentState.conditionImageCode,
                isDay = state.currentState.isDay,
                temp = state.currentState.temp,
            )
            else -> IActionBarState.Loading
        }
    }

    private fun asState(
        requestResult: RequestResult<List<Location>>
    ): IDashboardState = when(requestResult) {
        is RequestResult.Error -> IDashboardState.Error
        is RequestResult.InProgress -> IDashboardState.Loading
        is RequestResult.Success -> if (requestResult.data.isEmpty()) {
            IDashboardState.Empty
        } else {
            IDashboardState.Content(
                actionBarState = IActionBarState.Loading,
                forecasts = requestResult.data
            )
        }
    }
}
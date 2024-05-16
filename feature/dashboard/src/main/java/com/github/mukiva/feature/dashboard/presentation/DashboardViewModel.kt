package com.github.mukiva.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.dashboard.domain.model.Location
import com.github.mukiva.feature.dashboard.domain.usecase.GetAllLocationsUseCase
import com.github.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val getAllLocationsUseCase: GetAllLocationsUseCase,
    private val router: IDashboardRouter,
) : ViewModel(),
    IDashboardRouter by router,
    IDashboardViewModel {

    override val locationListState: StateFlow<DashboardState>
        get() = mState.asStateFlow()

    private val mState = MutableStateFlow<DashboardState>(DashboardState.Init)

    init {
        loadLocations()
    }

    override fun loadLocations() {
        getAllLocationsUseCase()
            .map(::asState)
            .onEach { mState.emit(it) }
            .launchIn(viewModelScope)
    }

    private fun asState(requestResult: RequestResult<List<Location>>): DashboardState {
        return when (requestResult) {
            is RequestResult.Error -> DashboardState.Error
            is RequestResult.InProgress -> DashboardState.Loading
            is RequestResult.Success -> stateContentFactory(checkNotNull(requestResult.data))
        }
    }

    private fun stateContentFactory(locations: List<Location>): DashboardState {
        return when {
            locations.isEmpty() -> DashboardState.Empty
            else -> DashboardState.Content(locations = locations)
        }
    }
}

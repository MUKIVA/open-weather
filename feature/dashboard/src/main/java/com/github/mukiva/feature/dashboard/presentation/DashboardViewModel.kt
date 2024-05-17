package com.github.mukiva.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.dashboard.domain.model.Location
import com.github.mukiva.feature.dashboard.domain.usecase.GetAllLocationsUseCase
import com.github.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val getAllLocationsUseCase: GetAllLocationsUseCase,
    private val router: IDashboardRouter,
) : ViewModel(),
    IDashboardRouter by router,
    IDashboardViewModel {

    override val locationListState: StateFlow<DashboardState>
        get() = getAllLocationsUseCase()
            .map(::asState)
            .stateIn(viewModelScope, SharingStarted.Lazily, DashboardState.Init)

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

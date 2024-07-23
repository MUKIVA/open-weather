package com.github.mukiva.feature.dashboard.presentation

import com.github.mukiva.feature.dashboard.domain.model.Location

sealed interface IDashboardState {

    val actionBarState: IActionBarState

    data object Loading : IDashboardState {
        override val actionBarState: IActionBarState
            get() = IActionBarState.Loading
    }
    data object Error : IDashboardState {
        override val actionBarState: IActionBarState
            get() = IActionBarState.Loading
    }

    data object Empty : IDashboardState {
        override val actionBarState: IActionBarState
            get() = IActionBarState.Loading
    }

    data class Content(
        override val actionBarState: IActionBarState,
        val forecasts: List<Location>
    ) : IDashboardState

}
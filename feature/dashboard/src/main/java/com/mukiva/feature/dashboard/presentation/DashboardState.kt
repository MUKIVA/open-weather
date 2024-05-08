package com.mukiva.feature.dashboard.presentation

import com.mukiva.feature.dashboard.domain.model.Location

sealed class DashboardState {
    data object Init : DashboardState()
    data object Loading : DashboardState()
    data object Error : DashboardState()
    data object Empty : DashboardState()
    data class Content(
        val locations: List<Location>,
    ) : DashboardState()
}


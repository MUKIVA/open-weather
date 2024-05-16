package com.github.mukiva.feature.dashboard.presentation

import kotlinx.coroutines.flow.StateFlow

interface IDashboardViewModel {

    val locationListState: StateFlow<DashboardState>
    fun loadLocations()
}
package com.presentation

import com.mukiva.api.navigation.ISettingsScreenProvider
import com.mukiva.core.navigation.INavigator
import com.mukiva.feature.location_manager_api.navigation.ILocationManagerScreenProvider
import com.mukiva.openweather.presentation.SingleStateViewModel
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    initialState: DashboardState,
    private val navigator: INavigator,
    private val settingsScreenProvider: ISettingsScreenProvider,
    private val locationManagerScreenProvider: ILocationManagerScreenProvider

) : SingleStateViewModel<DashboardState, Nothing>(initialState) {

    fun onSelectLocations() {
        navigator.launch(locationManagerScreenProvider.screen)
    }

    fun onSettings() {
        navigator.launch(settingsScreenProvider.screen)
    }

}
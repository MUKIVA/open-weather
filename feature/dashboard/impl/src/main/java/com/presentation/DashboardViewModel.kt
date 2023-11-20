package com.presentation

import com.mukiva.core.navigation.INavigator
import com.mukiva.openweather.presentation.SingleStateViewModel
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    initialState: DashboardState,
    private val navigator: INavigator
) : SingleStateViewModel<DashboardState, Nothing>(initialState) {

    fun onSelectLocations() {
//       TODO( navigator.launch(SettingsScreen()) )

    }

}
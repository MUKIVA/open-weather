package com.mukiva.impl.presentation

import com.mukiva.openweather.presentation.SingleStateViewModel
import javax.inject.Inject

internal class SettingsViewModel @Inject constructor(
    initialState: SettingsState
) : SingleStateViewModel<SettingsState, Nothing>(initialState) {


}
package com.github.mukiva.feature.locationmanager.presentation

import kotlinx.coroutines.flow.StateFlow

internal interface ILocationManagerAppBarHandler {
    val appBarStateFlow: StateFlow<ILocationManagerAppbarState>
    fun enterEditMode()
    fun enterNormalMode()
}


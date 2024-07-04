package com.github.mukiva.feature.locationmanager.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LocationManagerAppBarHandler @Inject constructor() : ILocationManagerAppBarHandler {
    override val appBarStateFlow: StateFlow<ILocationManagerAppbarState>
        get() = mAppBarStateFlow.asStateFlow()

    private val mAppBarStateFlow =
        MutableStateFlow<ILocationManagerAppbarState>(ILocationManagerAppbarState.Normal)
    override fun enterEditMode() {
        mAppBarStateFlow.tryEmit(ILocationManagerAppbarState.Edit)
    }
    override fun enterNormalMode() {
        mAppBarStateFlow.tryEmit(ILocationManagerAppbarState.Normal)
    }
}

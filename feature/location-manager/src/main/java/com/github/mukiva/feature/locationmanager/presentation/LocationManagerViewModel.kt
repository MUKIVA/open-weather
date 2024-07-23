package com.github.mukiva.feature.locationmanager.presentation

import androidx.lifecycle.ViewModel
import com.github.mukiva.feature.locationmanager.navigation.ILocationManagerRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class LocationManagerViewModel @Inject constructor(
    private val locationManagerRouter: ILocationManagerRouter,
    private val savedLocationsHandler: ISavedLocationsHandler,
    private val searchLocationHandler: ISearchLocationHandler,
    private val appBarHandler: ILocationManagerAppBarHandler
) :
    ViewModel(),
    ISavedLocationsHandler by savedLocationsHandler,
    ISearchLocationHandler by searchLocationHandler,
    ILocationManagerAppBarHandler by appBarHandler {

    fun goBack() = locationManagerRouter.goBack()

    override fun enterNormalMode() {
        savedLocationsHandler.enterNormalMode()
        appBarHandler.enterNormalMode()
    }

    override fun enterEditMode(location: EditableLocation) {
        appBarHandler.enterEditMode()
        savedLocationsHandler.enterEditMode(location)
    }
}

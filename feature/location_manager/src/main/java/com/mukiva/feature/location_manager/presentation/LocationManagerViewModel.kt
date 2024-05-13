package com.mukiva.feature.location_manager.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukiva.feature.location_manager.domain.model.Location
import com.mukiva.feature.location_manager.domain.usecase.AddLocationUseCase
import com.mukiva.feature.location_manager.navigation.ILocationManagerRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationManagerViewModel @Inject constructor(
    private val addLocationUseCase: AddLocationUseCase,
    private val locationManagerRouter: ILocationManagerRouter,
    private val savedLocationsHandler: ISavedLocationsHandler,
    private val searchLocationHandler: ISearchLocationHandler,
) : ViewModel(),
    ISavedLocationsHandler by savedLocationsHandler,
    ISearchLocationHandler by searchLocationHandler {
    fun addLocation(location: Location) {
        viewModelScope.launch {
            addLocationUseCase(location)
            fetchAddedLocations()
            filterSearchLocations(location)
        }
    }

    fun goBack() = locationManagerRouter.goBack()
}

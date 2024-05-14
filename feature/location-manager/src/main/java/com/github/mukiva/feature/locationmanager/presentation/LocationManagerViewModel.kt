package com.github.mukiva.feature.locationmanager.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.locationmanager.domain.model.Location
import com.github.mukiva.feature.locationmanager.domain.usecase.AddLocationUseCase
import com.github.mukiva.feature.locationmanager.navigation.ILocationManagerRouter
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

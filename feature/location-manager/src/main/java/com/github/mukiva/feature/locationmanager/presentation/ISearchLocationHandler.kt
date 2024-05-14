package com.github.mukiva.feature.locationmanager.presentation

import com.github.mukiva.feature.locationmanager.domain.model.Location
import kotlinx.coroutines.flow.StateFlow

interface ISearchLocationHandler {
    val searchLocationState: StateFlow<SearchLocationsState>
    fun filterSearchLocations(location: Location)
    fun executeSearch(q: String)
}

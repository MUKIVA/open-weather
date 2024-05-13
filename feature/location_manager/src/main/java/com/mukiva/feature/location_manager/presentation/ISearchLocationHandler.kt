package com.mukiva.feature.location_manager.presentation

import com.mukiva.feature.location_manager.domain.model.Location
import kotlinx.coroutines.flow.StateFlow

interface ISearchLocationHandler {
    val searchLocationState: StateFlow<SearchLocationsState>
    fun filterSearchLocations(location: Location)
    fun executeSearch(q: String)
}
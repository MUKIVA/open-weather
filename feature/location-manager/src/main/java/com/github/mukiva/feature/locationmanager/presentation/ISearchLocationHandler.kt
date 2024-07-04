package com.github.mukiva.feature.locationmanager.presentation

import com.github.mukiva.feature.locationmanager.domain.model.Location
import kotlinx.coroutines.flow.StateFlow

interface ISearchLocationHandler {
    val searchLocationState: StateFlow<ISearchLocationsState>
    fun onSearchQueryChanged(query: String?)
    fun retrySearch()
    fun addLocation(location: Location)
}

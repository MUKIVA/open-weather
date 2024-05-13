package com.mukiva.feature.location_manager.presentation

import com.mukiva.feature.location_manager.domain.model.Location

sealed class SearchLocationsState {
    data object Loading : SearchLocationsState()
    data object Error : SearchLocationsState()
    data object Empty : SearchLocationsState()
    data class Content(
        val data: List<Location>
    ) : SearchLocationsState()
}
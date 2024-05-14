package com.github.mukiva.feature.locationmanager.presentation

import com.github.mukiva.feature.locationmanager.domain.model.Location

sealed class SearchLocationsState {
    data object Loading : SearchLocationsState()
    data object Error : SearchLocationsState()
    data object Empty : SearchLocationsState()
    data class Content(
        val data: List<Location>
    ) : SearchLocationsState()
}

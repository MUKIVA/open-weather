package com.github.mukiva.feature.locationmanager.presentation

import com.github.mukiva.feature.locationmanager.domain.model.Location

sealed interface ISearchLocationsState {
    data object Loading : ISearchLocationsState
    data object Error : ISearchLocationsState
    data class Content(
        val data: List<Location>
    ) : ISearchLocationsState
}

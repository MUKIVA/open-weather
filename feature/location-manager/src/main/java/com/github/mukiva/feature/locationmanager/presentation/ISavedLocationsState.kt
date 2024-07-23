package com.github.mukiva.feature.locationmanager.presentation

internal sealed interface ISavedLocationsState {
    data object Loading : ISavedLocationsState
    data object Error : ISavedLocationsState
    data class Content(
        val data: List<EditableLocation>
    ) : ISavedLocationsState
}


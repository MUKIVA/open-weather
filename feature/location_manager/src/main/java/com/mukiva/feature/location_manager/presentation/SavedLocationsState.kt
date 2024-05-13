package com.mukiva.feature.location_manager.presentation

sealed class SavedLocationsState {
    data object Init : SavedLocationsState()
    data object Loading : SavedLocationsState()
    data object Error : SavedLocationsState()
    data object Empty : SavedLocationsState()
    data class Content(
        val data: List<EditableLocation>
    ) : SavedLocationsState()
    data class Edit(
        val data: List<EditableLocation>
    ) : SavedLocationsState()
}


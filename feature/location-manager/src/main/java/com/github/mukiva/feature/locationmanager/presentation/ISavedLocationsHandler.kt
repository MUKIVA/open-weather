package com.github.mukiva.feature.locationmanager.presentation

import kotlinx.coroutines.flow.StateFlow

interface ISavedLocationsHandler {
    val savedLocationsState: StateFlow<SavedLocationsState>
    fun enterEditMode(location: EditableLocation)
    fun fetchAddedLocations()
    fun enterNormalMode()
    fun removeSelectedLocations()
    fun removeLocation(editableLocation: EditableLocation)
    fun moveLocation(from: Int, to: Int)
    fun switchEditableSelect(editableLocation: EditableLocation)
    fun selectAllEditable()
}

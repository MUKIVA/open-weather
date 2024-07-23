package com.github.mukiva.feature.locationmanager.presentation

import kotlinx.coroutines.flow.StateFlow

internal interface ISavedLocationsHandler {
    val savedLocationsState: StateFlow<ISavedLocationsState>
    fun enterEditMode(location: EditableLocation)
    fun enterNormalMode()
    fun removeSelectedLocations()
    fun moveLocation(from: Int, to: Int)
    fun switchEditableSelect(editableLocation: EditableLocation)
    fun selectAllEditable()
    fun retryLoadSavedLocations()
}

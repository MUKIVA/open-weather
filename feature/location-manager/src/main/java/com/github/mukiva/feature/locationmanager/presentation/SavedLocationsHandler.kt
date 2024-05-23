package com.github.mukiva.feature.locationmanager.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.locationmanager.domain.usecase.GetAddedLocationsUseCase
import com.github.mukiva.feature.locationmanager.domain.usecase.UpdateStoredLocationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

class SavedLocationsHandler @Inject constructor(
    private val updateStoredLocationsUseCase: UpdateStoredLocationsUseCase,
    private val getAddedLocationsUseCase: GetAddedLocationsUseCase,
) : ViewModel(), ISavedLocationsHandler {
    override val savedLocationsState: StateFlow<SavedLocationsState>
        get() = mSavedLocationsState

    private val mSavedLocationsState =
        MutableStateFlow<SavedLocationsState>(SavedLocationsState.Init)

    override fun enterEditMode(location: EditableLocation) {
        val state = mSavedLocationsState.value as? SavedLocationsState.Content
            ?: return
        val selectedLocation = location.copy(
            isSelected = true,
            isEditable = true,
        )
        val newState = SavedLocationsState.Edit(
            state.data.map {
                if (selectedLocation.location.id == it.location.id) {
                    selectedLocation
                } else {
                    it.copy(isEditable = true)
                }
            }
        )
        mSavedLocationsState.update { newState }
    }
    override fun fetchAddedLocations() {
        viewModelScope.launch {
            getAddedLocationsUseCase()
                .map(::asSavedListState)
                .onEach(mSavedLocationsState::emit)
                .filter { it !is SavedLocationsState.Loading && it !is SavedLocationsState.Init }
                .first()
        }
    }
    override fun enterNormalMode() {
        val state = mSavedLocationsState.value as? SavedLocationsState.Edit
            ?: return
        val newState = SavedLocationsState.Content(
            state.data
                .map { it.copy(isEditable = false, isSelected = false) }
        )
        mSavedLocationsState.update { newState }
    }
    override fun removeSelectedLocations() {
        val state = mSavedLocationsState.value as? SavedLocationsState.Edit
            ?: return
        val newData = state.data.filter { editableLocation -> !editableLocation.isSelected }
        viewModelScope.launch { updateStoredLocationsUseCase(newData) }
        mSavedLocationsState.update { asSavedListState(newData) }
    }
    override fun removeLocation(editableLocation: EditableLocation) {
        val state = mSavedLocationsState.value as? SavedLocationsState.Edit
            ?: return
        val newData = state.data.filter { editableLocation.location.id != it.location.id }
        viewModelScope.launch { updateStoredLocationsUseCase(newData) }
        mSavedLocationsState.update { asSavedListState(newData) }
    }
    override fun moveLocation(from: Int, to: Int) {
        val state = mSavedLocationsState.value as? SavedLocationsState.Edit
            ?: return
        val newData = state.data.swapAll(from, to)
        viewModelScope.launch { updateStoredLocationsUseCase(newData) }
        mSavedLocationsState.update { state.copy(data = newData) }
    }
    override fun selectAllEditable() {
        val state = mSavedLocationsState.value as? SavedLocationsState.Edit
            ?: return
        val newData = state.data.map { editableLocation -> editableLocation.copy(isSelected = true) }
        viewModelScope.launch { updateStoredLocationsUseCase(newData) }
        mSavedLocationsState.update { state.copy(data = newData) }
    }
    override fun switchEditableSelect(editableLocation: EditableLocation) {
        val state = mSavedLocationsState.value as? SavedLocationsState.Edit
            ?: return
        var anyLocationIsSelected = false
        val newData = state.data.map { location ->
            if (editableLocation.location.id == location.location.id) {
                anyLocationIsSelected = anyLocationIsSelected || !location.isSelected
                location.copy(isSelected = !location.isSelected)
            } else {
                anyLocationIsSelected = anyLocationIsSelected || location.isSelected
                location
            }
        }
        mSavedLocationsState.update { asSavedListState(newData) }
    }
    private fun List<EditableLocation>.swapAll(from: Int, to: Int): List<EditableLocation> {
        if (from < to) {
            for (i in from until to) {
                Collections.swap(this, i, i + 1)
            }
        } else {
            for (i in from downTo to + 1) {
                Collections.swap(this, i, i - 1)
            }
        }
        return this
    }
}

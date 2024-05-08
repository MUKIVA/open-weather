package com.mukiva.feature.location_manager.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.weather_data.utils.RequestResult
import com.mukiva.feature.location_manager.navigation.ILocationManagerRouter
import com.mukiva.feature.location_manager.domain.model.Location
import com.mukiva.feature.location_manager.domain.repository.IForecastUpdater
import com.mukiva.feature.location_manager.domain.usecase.AddLocationUseCase
import com.mukiva.feature.location_manager.domain.usecase.GetAddedLocationsUseCase
import com.mukiva.feature.location_manager.domain.usecase.LocationSearchUseCase
import com.mukiva.feature.location_manager.domain.usecase.UpdateStoredLocationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class LocationManagerViewModel @Inject constructor(
    private val searchUseCase: LocationSearchUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val getAddedLocationsUseCase: GetAddedLocationsUseCase,
    private val updateStoredLocationsUseCase: UpdateStoredLocationsUseCase,
    private val locationManagerRouter: ILocationManagerRouter,
    private val forecastUpdater: IForecastUpdater
) : ViewModel() {
    val savedLocationsState: StateFlow<SavedLocationsState>
        get() = mSavedLocationsState

    val searchLocationState: StateFlow<SearchLocationsState>
        get() = mSearchLocationsState

    private val mSavedLocationsState =
        MutableStateFlow<SavedLocationsState>(SavedLocationsState.Init)
    private val mSearchLocationsState =
        MutableStateFlow<SearchLocationsState>(SearchLocationsState.Empty)

    fun addLocation(location: Location) {
        viewModelScope.launch {
            addLocationUseCase(location)
            fetchAddedLocations()
            mSearchLocationsState.update {  state ->
                if (state is SearchLocationsState.Content) {
                    state.copy(data = state.data.filter { it.id != location.id })
                } else state
            }
        }
    }
    fun enterEditMode(location: EditableLocation) {
        mSavedLocationsState.update { state ->
            if (state is SavedLocationsState.Content) {
                val selectedLocation = location.copy(
                    isSelected = true,
                    isEditable = true,
                )
                SavedLocationsState.Edit(state.data
                    .map {
                        if (selectedLocation.location.id == it.location.id)
                            selectedLocation
                        else it.copy(isEditable = true)
                    }
                )
            } else state
        }
    }
    fun enterNormalMode() {
        mSavedLocationsState.update { state ->
            if (state is SavedLocationsState.Edit) {
                SavedLocationsState.Content(state.data
                    .map { it.copy(isEditable = false, isSelected = false) }
                )
            } else state
        }
    }
    fun removeSelectedLocations() {
        mSavedLocationsState.update { state -> if (state is SavedLocationsState.Edit) {
            val newData = state.data.filter { editableLocation -> !editableLocation.isSelected }
            viewModelScope.launch { updateStoredLocationsUseCase(newData) }
            savedLocationsStateFactory(newData)
        } else state }
    }
    fun removeLocation(editableLocation: EditableLocation) {
        mSavedLocationsState.update { state -> if (state is SavedLocationsState.Edit) {
            val newData = state.data.filter { editableLocation.location.id != it.location.id }
            viewModelScope.launch { updateStoredLocationsUseCase(newData) }
            savedLocationsStateFactory(newData)
        } else state }
    }
    fun moveLocation(from: Int, to: Int) {
        mSavedLocationsState.update { state -> if (state is SavedLocationsState.Edit) {
            val newData = state.data.swapAll(from, to)
            viewModelScope.launch { updateStoredLocationsUseCase(newData) }
            state.copy(data = newData)
        } else state }
    }
    fun executeSearch(q: String) {
        if (q.isBlank()) {
            mSearchLocationsState.update { SearchLocationsState.Empty }
            return
        }
        searchUseCase(q)
            .onEach {
                Log.d("SEARCH", it.toString())
                applySearchListState(it)
            }
            .launchIn(viewModelScope)
    }

    fun selectAllEditable() {
        mSavedLocationsState.update { state -> if (state is SavedLocationsState.Edit) {
            val newData = state.data.map { editableLocation -> editableLocation.copy(isSelected = true) }
            viewModelScope.launch { updateStoredLocationsUseCase(newData) }
            state.copy(data = newData)
        } else state }
    }

    fun switchEditableSelect(editableLocation: EditableLocation) {
        mSavedLocationsState.update { state -> if (state is SavedLocationsState.Edit) {
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
            savedLocationsStateFactory(newData)
        } else state }
    }

    fun goBack() = locationManagerRouter.goBack()

    fun fetchAddedLocations() {
        getAddedLocationsUseCase()
            .onEach(::applySavedListState)
            .launchIn(viewModelScope)
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

    private fun applySavedListState(state: RequestResult<List<EditableLocation>>) {
        when(state) {
            is RequestResult.Error -> mSavedLocationsState
                .update { SavedLocationsState.Error }
            is RequestResult.InProgress -> mSavedLocationsState
                .update { SavedLocationsState.Loading }
            is RequestResult.Success -> mSavedLocationsState.update {
                val data = checkNotNull(state.data)
                if (data.isEmpty())
                    SavedLocationsState.Empty
                else
                    SavedLocationsState.Content(data)
            }
        }
    }

    private fun applySearchListState(state: RequestResult<List<Location>>) {
        when(state) {
            is RequestResult.Error ->
                mSearchLocationsState.update { SearchLocationsState.Error }
            is RequestResult.InProgress ->
                mSearchLocationsState.update { SearchLocationsState.Loading }
            is RequestResult.Success -> mSearchLocationsState.update {
                val data = checkNotNull(state.data)
                if (data.isEmpty())
                    SearchLocationsState.Empty
                else
                    SearchLocationsState.Content(data)
            }
        }
    }

    private fun savedLocationsStateFactory(data: List<EditableLocation>): SavedLocationsState {
        return when {
            data.isEmpty() -> SavedLocationsState.Empty
            data.any { it.isSelected } ->
                SavedLocationsState.Edit(data)
            else ->
                SavedLocationsState.Content(data.map { it.copy(isEditable = false) })
        }
    }
}
package com.mukiva.feature.location_manager_impl.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.feature.location_manager_impl.domain.model.Location
import com.mukiva.feature.location_manager_impl.domain.usecase.AddLocationUseCase
import com.mukiva.feature.location_manager_impl.domain.usecase.GetAddedLocationsUseCase
import com.mukiva.feature.location_manager_impl.domain.usecase.LocationSearchUseCase
import com.mukiva.feature.location_manager_impl.domain.usecase.UpdateStoredLocationsUseCase
import com.mukiva.openweather.presentation.SingleStateViewModel
import com.mukiva.usecase.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class LocationManagerViewModel @Inject constructor(
    initialState: LocationManagerState,
    private val searchUseCase: LocationSearchUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val getAddedLocationsUseCase: GetAddedLocationsUseCase,
    private val updateStoredLocationsUseCase: UpdateStoredLocationsUseCase
) : SingleStateViewModel<LocationManagerState, LocationManagerEvent>(initialState) {

    init {
        fetchAddedLocations()
    }

    fun addLocation(location: Location) {
        viewModelScope.launch {
           when (val result = addLocationUseCase(location)) {
               is ApiResult.Error -> {
                   event(LocationManagerEvent.Toast("FAIL: ${result.err}"))
               }
               is ApiResult.Success -> {
                   val locations = state.value.searchListState.list
                   updateSearchListState(locations.filter {
                       it.uid != location.uid
                   })
                   fetchAddedLocations()
                   event(LocationManagerEvent.Toast("SUCCESS ADD"))
               }
           }
        }
    }
    fun removeSelectedLocations() {
        with(state.value) {
            val newSavedListSate = savedListState.copy(
                list = savedListState.list.filter { !it.isSelected }
            )
            viewModelScope.launch {
                updateStoredLocationsUseCase(newSavedListSate.list)
            }
            modifyState { copy(savedListState = newSavedListSate) }
            validateEditState(newSavedListSate)
        }
    }
    fun removeLocation(item: EditableLocation) {
        with(state.value) {
            val newSavedListState = savedListState.copy(
                list = savedListState.list.filter { it.location.uid != item.location.uid }
            )
            modifyState {
                copy(savedListState = newSavedListState)
            }
            validateEditState(newSavedListState)
        }

    }
    fun moveLocation(from: Int, to: Int) {
        with(state.value) {
            val newState = savedListState.copy(
                list = savedListState.list.swapAll(from, to)
            )
            viewModelScope.async {
                updateStoredLocationsUseCase(newState.list)
            }.invokeOnCompletion {
                modifyState {
                    copy(
                        savedListState = newState
                    )
                }
            }

        }
    }
    fun executeSearch(q: String) {
        setSearchListType(LocationManagerState.ListStateType.LOADING)

        if (q == "") {
            setSearchListType(LocationManagerState.ListStateType.EMPTY)
            return
        }

        viewModelScope.launch {
            when (val result = searchUseCase(q)) {
                is ApiResult.Error -> {
                    setSearchListType(LocationManagerState.ListStateType.ERROR)
                }
                is ApiResult.Success -> {
                    updateSearchListState(result.data)
                }
            }
        }

    }
    fun enterSearchMode() =
        setManagerType(LocationManagerState.Type.SEARCH)
    fun enterNormalMode() {
        setManagerType(LocationManagerState.Type.NORMAL)
        updateIsEditableForAllLocations(false)
    }
    fun enterEditMode(item: EditableLocation) {
        setManagerType(LocationManagerState.Type.EDIT)
        updateIsEditableForAllLocations(true, item)
    }
    fun selectAllEditable() {
        modifyState {
            copy(
                savedListState = savedListState.copy(
                    list = savedListState.list.map { it.copy(isSelected = true) }
                )
            )
        }
    }
    fun switchEditableSelect(item: EditableLocation) {
        with(state.value) {
            val newSavedListState = savedListState.copy(
                list = savedListState.list.map {
                    when (it.location.uid == item.location.uid) {
                        true -> it.copy(isSelected = !it.isSelected)
                        false -> it
                    }
                }
            )
            modifyState {
                copy(savedListState = newSavedListState)
            }
            validateEditState(newSavedListState)
        }
    }
    private fun updateIsEditableForAllLocations(isEditable: Boolean, item: EditableLocation? = null) {
        modifyState {
            copy(
                savedListState = savedListState.copy(
                    list = savedListState.list.map {
                        it.copy(
                            isEditable = isEditable,
                            isSelected = item?.run {
                                location.uid == it.location.uid
                            } ?: false
                        )
                    }
                )
            )
        }

    }
    private fun validateEditState(listState: ListState<EditableLocation>) = with(EditableLocation) {
        if (listState.selectedCount == 0) {
            enterNormalMode()
        }
        if (listState.list.isEmpty()) {
            modifyState {
                copy(savedListState = savedListState.copy(
                    type = LocationManagerState.ListStateType.EMPTY
                ))
            }
        }
    }
    private fun setManagerType(type: LocationManagerState.Type) {
        modifyState {
            copy(type = type)
        }
    }
    private fun fetchAddedLocations() {
        viewModelScope.launch {
            when (val result = getAddedLocationsUseCase()) {
                is ApiResult.Success -> {
                    updateAddedListState(result.data.map { it.asEditable() })
                }
                is ApiResult.Error -> {
                    event(LocationManagerEvent.Toast(result.err.name))
                }
            }
        }
    }
    private fun updateAddedListState(list: List<EditableLocation>) {
        modifyState {
            copy(
                savedListState = savedListState.copy(
                    list = list,
                    type = when(list.size) {
                        0 -> LocationManagerState.ListStateType.EMPTY
                        else -> LocationManagerState.ListStateType.CONTENT
                    }
                )
            )
        }
    }
    private fun updateSearchListState(list: List<Location>) {
        modifyState {
            copy(
                searchListState = searchListState.copy(
                    list = list,
                    type = when(list.size) {
                        0 -> LocationManagerState.ListStateType.EMPTY
                        else -> LocationManagerState.ListStateType.CONTENT
                    }
                )
            )
        }
    }
    private fun setSearchListType(type: LocationManagerState.ListStateType) {
        modifyState {
            copy(
                searchListState = searchListState.copy(
                    type = type
                )
            )
        }
    }
    private fun Location.asEditable() = EditableLocation(
        location = this,
        isSelected = false,
        isEditable = state.value.type == LocationManagerState.Type.EDIT
    )
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
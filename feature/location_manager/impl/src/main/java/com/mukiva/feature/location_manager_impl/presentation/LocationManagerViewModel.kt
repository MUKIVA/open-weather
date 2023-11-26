package com.mukiva.feature.location_manager_impl.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.feature.location_manager_impl.domain.model.Location
import com.mukiva.feature.location_manager_impl.domain.usecase.AddLocationUseCase
import com.mukiva.feature.location_manager_impl.domain.usecase.GetAddedLocationsUseCase
import com.mukiva.feature.location_manager_impl.domain.usecase.LocationSearchUseCase
import com.mukiva.openweather.presentation.SingleStateViewModel
import com.mukiva.usecase.ApiResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationManagerViewModel @Inject constructor(
    initialState: LocationManagerState,
    private val searchUseCase: LocationSearchUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val getAddedLocationsUseCase: GetAddedLocationsUseCase
) : SingleStateViewModel<LocationManagerState, LocationManagerEvent>(initialState) {

    init {
        fetchAddedLocations()
    }

    private fun fetchAddedLocations() {
        viewModelScope.launch {
            when (val result = getAddedLocationsUseCase()) {
                is ApiResult.Success -> {
                    updateAddedListState(result.data)
                }
                is ApiResult.Error -> {
                    event(LocationManagerEvent.Toast(result.err.name))
                }
            }
        }
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

    private fun updateAddedListState(list: List<Location>) {
        modifyState {
            copy(
                addedListState = addedListState.copy(
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

}
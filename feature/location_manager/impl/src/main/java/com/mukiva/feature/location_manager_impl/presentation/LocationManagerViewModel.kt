package com.mukiva.feature.location_manager_impl.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.feature.location_manager_impl.domain.model.Location
import com.mukiva.feature.location_manager_impl.domain.usecase.AddLocationUseCase
import com.mukiva.feature.location_manager_impl.domain.usecase.LocationSearchUseCase
import com.mukiva.openweather.presentation.SingleStateViewModel
import com.mukiva.usecase.ApiResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
class LocationManagerViewModel @Inject constructor(
    initialState: LocationManagerState,
    private val searchUseCase: LocationSearchUseCase,
    private val addLocationUseCase: AddLocationUseCase
) : SingleStateViewModel<LocationManagerState, LocationManagerEvent>(initialState) {

    private val mSearchQueryFlow = MutableSharedFlow<String>()

    init {
        mSearchQueryFlow
            .debounce(QUERY_UPDATE_DEBOUNCE)
            .onEach {
                modifyState { copy(query = it) }
                executeSearch(it)
            }
            .launchIn(viewModelScope)

    }

    fun clearQuery() {
        viewModelScope.launch {
            mSearchQueryFlow.emit("")
        }
    }

    fun updateSearchQuery(q: String) {

        if (state.value.query == q)
            return

        viewModelScope.launch {
            mSearchQueryFlow.emit(q)
        }
    }

    fun addLocation(location: Location) {
        viewModelScope.launch {
           when (val result = addLocationUseCase(location)) {
               is ApiResult.Error -> {
                   event(LocationManagerEvent.Toast("FAIL: ${result.err}"))
               }
               is ApiResult.Success -> {
                   val locations = state.value.receivedLocations
                   updateListState(locations.filter {
                       it.uid != location.uid
                   })
                   event(LocationManagerEvent.Toast("SUCCESS ADD"))
               }
           }
        }
    }

    private fun updateListState(list: List<Location>) {
        modifyState {
            copy(
                receivedLocations = list,
                listState = when(list.size) {
                    0 -> LocationManagerState.ListState.EMPTY
                    else -> LocationManagerState.ListState.CONTENT
                }
            )}
    }

    fun executeSearch(q: String) {
        modifyState { copy(listState = LocationManagerState.ListState.LOADING) }

        if (q == "") {
            modifyState { copy(listState = LocationManagerState.ListState.EMPTY) }
            return
        }

        viewModelScope.launch {
            when (val result = searchUseCase(q)) {
                is ApiResult.Error -> {
                    modifyState {
                        copy(
                            listState = LocationManagerState.ListState.ERROR
                        )
                    }
                }
                is ApiResult.Success -> {
                    updateListState(result.data)
                }
            }
        }

    }

    companion object {
        private const val QUERY_UPDATE_DEBOUNCE = 500L
    }

}
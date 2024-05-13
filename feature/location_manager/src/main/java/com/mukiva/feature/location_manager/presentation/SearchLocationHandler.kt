package com.mukiva.feature.location_manager.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukiva.feature.location_manager.domain.model.Location
import com.mukiva.feature.location_manager.domain.usecase.LocationSearchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SearchLocationHandler @Inject constructor(
    private val searchUseCase: LocationSearchUseCase,
) : ViewModel(), ISearchLocationHandler {
    override val searchLocationState: StateFlow<SearchLocationsState>
        get() = mSearchLocationsState

    private val mSearchLocationsState =
        MutableStateFlow<SearchLocationsState>(SearchLocationsState.Empty)

    override fun executeSearch(q: String) {
        if (q.isBlank()) {
            mSearchLocationsState.update { SearchLocationsState.Empty }
            return
        }
        searchUseCase(q)
            .map(::asSearchListState)
            .onEach(mSearchLocationsState::emit)
            .launchIn(viewModelScope)
    }

    override fun filterSearchLocations(location: Location) {
        mSearchLocationsState.update { state ->
            if (state is SearchLocationsState.Content) {
                state.copy(data = state.data.filter { it.id != location.id })
            } else {
                state
            }
        }
    }
}
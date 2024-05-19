package com.github.mukiva.feature.locationmanager.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.locationmanager.domain.usecase.LocationSearchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            searchUseCase(q)
                .map(::asSearchListState)
                .onEach(mSearchLocationsState::emit)
                .launchIn(viewModelScope)
        }
    }

    override fun filterSearchLocations(location: com.github.mukiva.feature.locationmanager.domain.model.Location) {
        mSearchLocationsState.update { state ->
            if (state is SearchLocationsState.Content) {
                state.copy(data = state.data.filter { it.id != location.id })
            } else {
                state
            }
        }
    }
}

package com.mukiva.location_search.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.location_search.domain.LocationPoint
import com.mukiva.location_search.domain.usecase.SearchUseCase
import com.mukiva.openweather.presentation.SingleStateViewModel
import com.mukiva.usecase.ApiResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
class SearchViewModel @Inject constructor(
    initialState: SearchLocationState,
    private val searchUseCase: SearchUseCase
) : SingleStateViewModel<SearchLocationState, Nothing>(initialState) {

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

    fun updateSearchQuery(q: String) {

        if (state.value.query == q)
            return

        viewModelScope.launch {
            mSearchQueryFlow.emit(q)
        }
    }

    fun addLocation(location: LocationPoint) {
        // TODO("Implement this method")
    }

    fun executeSearch(q: String) {
        modifyState { copy(listState = SearchLocationState.ListState.LOADING) }

        if (q == "") {
            modifyState { copy(listState = SearchLocationState.ListState.EMPTY) }
            return
        }

        viewModelScope.launch {
            when (val result = searchUseCase(q)) {
                is ApiResult.Error -> {
                    modifyState {
                        copy(
                            listState = SearchLocationState.ListState.ERROR
                        )
                    }
                }
                is ApiResult.Success -> {
                    modifyState {
                        copy(
                            listState = when(result.data.size) {
                                0 -> SearchLocationState.ListState.EMPTY
                                else -> SearchLocationState.ListState.CONTENT
                            },
                            receivedLocations = result.data
                        )
                    }
                }
            }
        }

    }

    companion object {
        private const val QUERY_UPDATE_DEBOUNCE = 500L
    }

}
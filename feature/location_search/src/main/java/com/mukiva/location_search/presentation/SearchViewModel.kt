package com.mukiva.location_search.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.location_search.domain.model.Location
import com.mukiva.location_search.domain.usecase.AddLocationUseCase
import com.mukiva.location_search.domain.usecase.SearchUseCase
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
class SearchViewModel @Inject constructor(
    initialState: SearchLocationState,
    private val searchUseCase: SearchUseCase,
    private val addLocationUseCase: AddLocationUseCase
) : SingleStateViewModel<SearchLocationState, SearchEvent>(initialState) {

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
                   event(SearchEvent.Toast("FAIL: ${result.err}"))
               }
               is ApiResult.Success -> {
                   val locations = state.value.receivedLocations
                   updateListState(locations.filter {
                       it.uid != location.uid
                   })
                   event(SearchEvent.Toast("SUCCESS ADD"))
               }
           }
        }
    }

    private fun updateListState(list: List<Location>) {
        modifyState {
            copy(
                receivedLocations = list,
                listState = when(list.size) {
                    0 -> SearchLocationState.ListState.EMPTY
                    else -> SearchLocationState.ListState.CONTENT
                }
            )}
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
                    updateListState(result.data)
                }
            }
        }

    }

    companion object {
        private const val QUERY_UPDATE_DEBOUNCE = 500L
    }

}
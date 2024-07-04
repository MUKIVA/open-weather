package com.github.mukiva.feature.locationmanager.presentation

import com.github.mukiva.feature.locationmanager.domain.model.Location
import com.github.mukiva.feature.locationmanager.domain.usecase.AddLocationUseCase
import com.github.mukiva.feature.locationmanager.domain.usecase.LocationSearchUseCase
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("OPT_IN_USAGE")
class SearchLocationHandler @Inject constructor(
    private val searchUseCase: LocationSearchUseCase,
    private val addLocationUseCase: AddLocationUseCase
) : ISearchLocationHandler {

    override val searchLocationState: StateFlow<ISearchLocationsState>
        get() = mSearchLocationState.asStateFlow()

    private val mHandlerScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val mSearchLocationState =
        MutableStateFlow<ISearchLocationsState>(ISearchLocationsState.Content(emptyList()))

    private val mSearchFlow = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var mSearchJob: Job? = null

    init {
        mSearchFlow
            .debounce(SEARCH_DEBOUNCE_MS)
            .onEach(::executeSearch)
            .launchIn(mHandlerScope)
    }

    override fun onSearchQueryChanged(query: String?) {
        mSearchFlow.tryEmit(query ?: "")
    }

    override fun retrySearch() {
        mHandlerScope.launch {
            executeSearch(query = mSearchFlow.last())
        }
    }

    override fun addLocation(location: Location) {
        mHandlerScope.launch {
            val addRequest = async {
                addLocationUseCase(location)
            }

            val result = addRequest.await()

            if (result is RequestResult.Success) {
                val state = mSearchLocationState.value as? ISearchLocationsState.Content
                    ?: return@launch
                val newData = state.data.filter { location.id != it.id }
                mSearchLocationState
                    .tryEmit(ISearchLocationsState.Content(newData))
            }
        }
    }

    private fun executeSearch(query: String) {
        if (query.isEmpty()) {
            mSearchLocationState.tryEmit(
                ISearchLocationsState.Content(emptyList())
            )
            return
        }

        mSearchJob?.cancel()
        mSearchJob = mHandlerScope.launch {
            searchUseCase(query)
                .map(::searchRequestAsState)
                .collect(mSearchLocationState::emit)
        }
    }

    private fun searchRequestAsState(
        requestResult: RequestResult<List<Location>>
    ): ISearchLocationsState {
        return when (requestResult) {
            is RequestResult.Error -> ISearchLocationsState.Error
            is RequestResult.InProgress -> ISearchLocationsState.Loading
            is RequestResult.Success -> ISearchLocationsState.Content(requestResult.data)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 500L
    }
}

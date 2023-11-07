package com.mukiva.location_search.presentation

import com.mukiva.location_search.domain.LocationPoint

data class SearchLocationState(
    val listState: ListState,
    val query: String,
    val receivedLocations: List<LocationPoint>
) {

    enum class ListState {
        ERROR,
        EMPTY,
        LOADING,
        CONTENT
    }

    companion object {
        fun default() = SearchLocationState(
            listState = ListState.EMPTY,
            query = "",
            receivedLocations = emptyList()
        )
    }

}

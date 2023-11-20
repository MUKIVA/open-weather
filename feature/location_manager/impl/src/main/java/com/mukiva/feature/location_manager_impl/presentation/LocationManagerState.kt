package com.mukiva.feature.location_manager_impl.presentation

import com.mukiva.feature.location_manager_impl.domain.model.Location


data class LocationManagerState(
    val listState: ListState,
    val query: String,
    val receivedLocations: List<Location>
) {

    enum class ListState {
        ERROR,
        EMPTY,
        LOADING,
        CONTENT
    }

    companion object {
        fun default() = LocationManagerState(
            listState = ListState.EMPTY,
            query = "",
            receivedLocations = emptyList()
        )
    }

}

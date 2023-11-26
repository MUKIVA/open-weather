package com.mukiva.feature.location_manager_impl.presentation

import com.mukiva.feature.location_manager_impl.domain.model.Location

data class LocationManagerState(
    val searchListState: ListState<Location>,
    val addedListState: ListState<Location>,
    val query: String,
) {

    enum class ListStateType {
        ERROR,
        EMPTY,
        LOADING,
        CONTENT
    }

    companion object {
        fun default() = LocationManagerState(
            query = "",
            searchListState = ListState(
                type = ListStateType.EMPTY,
                list = emptyList()
            ),
            addedListState = ListState(
                type = ListStateType.EMPTY,
                list = emptyList()
            )
        )
    }

}

package com.mukiva.feature.location_manager.presentation

import com.mukiva.feature.location_manager.domain.model.Location

data class LocationManagerState(
    val type: Type,
    val searchListState: ListState<Location>,
    val savedListState: ListState<EditableLocation>,
    val query: String
) {
    enum class Type {
        NORMAL,
        SEARCH,
        EDIT
    }

    enum class ListStateType {
        ERROR,
        EMPTY,
        LOADING,
        CONTENT
    }

    companion object {
        fun default() = LocationManagerState(
            type = Type.NORMAL,
            query = "",
            searchListState = ListState(
                type = ListStateType.EMPTY,
                list = emptyList()
            ),
            savedListState = ListState(
                type = ListStateType.EMPTY,
                list = emptyList()
            )
        )
    }

}

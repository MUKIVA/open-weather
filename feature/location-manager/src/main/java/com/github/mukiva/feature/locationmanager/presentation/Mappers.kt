package com.github.mukiva.feature.locationmanager.presentation

import com.github.mukiva.feature.locationmanager.domain.model.Location
import com.github.mukiva.weatherdata.utils.RequestResult

internal fun asSavedListState(
    state: RequestResult<List<EditableLocation>>
): SavedLocationsState {
    return when (state) {
        is RequestResult.Error -> SavedLocationsState.Error
        is RequestResult.InProgress -> SavedLocationsState.Loading
        is RequestResult.Success -> {
            val data = checkNotNull(state.data)
            if (data.isEmpty()) {
                SavedLocationsState.Empty
            } else {
                SavedLocationsState.Content(data)
            }
        }
    }
}

internal fun asSavedListState(data: List<EditableLocation>): SavedLocationsState {
    return when {
        data.isEmpty() -> SavedLocationsState.Empty
        data.any { it.isSelected } ->
            SavedLocationsState.Edit(data)

        else ->
            SavedLocationsState.Content(data.map { it.copy(isEditable = false) })
    }
}

internal fun asSearchListState(
    state: RequestResult<List<Location>>
): SearchLocationsState {
    return when (state) {
        is RequestResult.Error -> SearchLocationsState.Error
        is RequestResult.InProgress -> SearchLocationsState.Loading
        is RequestResult.Success -> {
            val data = checkNotNull(state.data)
            if (data.isEmpty()) {
                SearchLocationsState.Empty
            } else {
                SearchLocationsState.Content(data)
            }
        }
    }
}

package com.mukiva.feature.location_manager_impl.presentation

data class ListState<TItem>(
    val type: LocationManagerState.ListStateType,
    val list: List<TItem>
)
package com.mukiva.feature.location_manager.presentation

data class ListState<TItem>(
    val type: LocationManagerState.ListStateType,
    val list: List<TItem>
)
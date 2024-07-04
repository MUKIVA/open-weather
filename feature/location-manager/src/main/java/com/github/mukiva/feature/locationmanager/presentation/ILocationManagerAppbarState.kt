package com.github.mukiva.feature.locationmanager.presentation

sealed interface ILocationManagerAppbarState {
    data object Edit : ILocationManagerAppbarState
    data object Normal : ILocationManagerAppbarState
}

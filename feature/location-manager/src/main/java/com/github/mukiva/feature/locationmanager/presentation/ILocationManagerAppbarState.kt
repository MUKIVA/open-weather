package com.github.mukiva.feature.locationmanager.presentation

internal sealed interface ILocationManagerAppbarState {
    data object Edit : ILocationManagerAppbarState
    data object Normal : ILocationManagerAppbarState
}

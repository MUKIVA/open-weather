package com.github.mukiva.feature.locationmanager.di

import com.github.mukiva.feature.locationmanager.presentation.ILocationManagerAppBarHandler
import com.github.mukiva.feature.locationmanager.presentation.ISavedLocationsHandler
import com.github.mukiva.feature.locationmanager.presentation.ISearchLocationHandler
import com.github.mukiva.feature.locationmanager.presentation.LocationManagerAppBarHandler
import com.github.mukiva.feature.locationmanager.presentation.SavedLocationsHandler
import com.github.mukiva.feature.locationmanager.presentation.SearchLocationHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ILocationManagerBinds {
    @Binds
    fun bindSavedLocationHandler(
        savedLocationsHandler: SavedLocationsHandler
    ): ISavedLocationsHandler

    @Binds
    fun bindSearchLocationHandler(
        searchLocationHandler: SearchLocationHandler
    ): ISearchLocationHandler

    @Binds
    fun bindAppBarHandler(
        locationManagerAppBarHandler: LocationManagerAppBarHandler
    ): ILocationManagerAppBarHandler
}

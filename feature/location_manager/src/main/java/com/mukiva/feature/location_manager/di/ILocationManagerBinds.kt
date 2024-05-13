package com.mukiva.feature.location_manager.di

import com.mukiva.feature.location_manager.presentation.ISavedLocationsHandler
import com.mukiva.feature.location_manager.presentation.ISearchLocationHandler
import com.mukiva.feature.location_manager.presentation.SavedLocationsHandler
import com.mukiva.feature.location_manager.presentation.SearchLocationHandler
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
}
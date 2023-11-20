package com.mukiva.feature.location_manager_impl.di

import androidx.lifecycle.ViewModel
import com.mukiva.feature.location_manager_impl.presentation.LocationManagerViewModel
import com.mukiva.openweather.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface LocationManagerBinds {

    @Binds
    @[IntoMap ViewModelKey(LocationManagerViewModel::class)]
    fun bindSearchLocationViewModel(vm: LocationManagerViewModel): ViewModel

}
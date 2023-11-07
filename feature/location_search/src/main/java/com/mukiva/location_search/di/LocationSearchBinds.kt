package com.mukiva.location_search.di

import androidx.lifecycle.ViewModel
import com.mukiva.location_search.presentation.SearchViewModel
import com.mukiva.openweather.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface LocationSearchBinds {

    @Binds
    @[IntoMap ViewModelKey(SearchViewModel::class)]
    fun bindSearchLocationViewModel(vm: SearchViewModel): ViewModel

}
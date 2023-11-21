package com.di

import androidx.lifecycle.ViewModel
import com.mukiva.openweather.core.di.ViewModelKey
import com.presentation.DashboardViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface IDashboardBinds {

    @Binds
    @[IntoMap ViewModelKey(DashboardViewModel::class)]
    fun bindCurrentWeatherStore(viewModel: DashboardViewModel): ViewModel

}
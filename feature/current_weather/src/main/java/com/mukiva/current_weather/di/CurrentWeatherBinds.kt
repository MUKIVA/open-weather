package com.mukiva.current_weather.di

import androidx.lifecycle.ViewModel
import com.mukiva.current_weather.presentation.CurrentWeatherViewModel
import com.mukiva.openweather.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CurrentWeatherBinds {

    @Binds
    @[IntoMap ViewModelKey(CurrentWeatherViewModel::class)]
    fun bindCurrentWeatherStore(viewModel: CurrentWeatherViewModel): ViewModel

}
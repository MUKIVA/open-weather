package com.mukiva.impl.di

import androidx.lifecycle.ViewModel
import com.mukiva.impl.presentation.SettingsViewModel
import com.mukiva.openweather.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal interface SettingsBinds {
    @Binds
    @[IntoMap ViewModelKey(SettingsViewModel::class)]
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

}
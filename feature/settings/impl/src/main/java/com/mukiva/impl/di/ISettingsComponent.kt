package com.mukiva.impl.di

import com.mukiva.openweather.core.di.MultiViewModelFactory
import dagger.Component

@Component(
    modules = [ISettingsBinds::class, SettingsModule::class],
    dependencies = [ISettingsDependencies::class]
)
interface ISettingsComponent {

    val factory: MultiViewModelFactory

    companion object {

        private var mInstance: ISettingsComponent? = null

        fun init(deps: ISettingsDependencies) {
            mInstance = DaggerISettingsComponent.builder()
                .iSettingsDependencies(deps)
                .build()
        }

        fun get(): ISettingsComponent {
            return mInstance!!
        }
    }
}
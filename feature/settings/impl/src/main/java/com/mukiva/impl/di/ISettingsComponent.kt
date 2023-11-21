package com.mukiva.impl.di

import com.mukiva.openweather.core.di.MultiViewModelFactory
import dagger.Component

@Component(
    modules = [ISettingsBinds::class, SettingsModule::class],
    dependencies = []
)
interface ISettingsComponent {

    val factory: MultiViewModelFactory

    companion object {

        private var mInstance: ISettingsComponent? = null

        fun init() {
            mInstance = DaggerISettingsComponent.builder()
                .build()
        }

        fun get(): ISettingsComponent {
            return mInstance!!
        }
    }
}
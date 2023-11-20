package com.mukiva.impl.di

import com.mukiva.openweather.core.di.MultiViewModelFactory
import dagger.Component

@Component(
    modules = [SettingsBinds::class, SettingsModule::class],
    dependencies = []
)
internal interface SettingsComponent {

    val factory: MultiViewModelFactory

    companion object {

        private var mInstance: SettingsComponent? = null

        private fun init(): SettingsComponent {
            mInstance = DaggerSettingsComponent.builder()
                .build()
            return mInstance!!
        }

        fun get(): SettingsComponent {
            return mInstance ?: init()
        }
    }
}
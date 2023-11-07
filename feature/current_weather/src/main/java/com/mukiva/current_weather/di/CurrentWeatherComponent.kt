package com.mukiva.current_weather.di

import com.mukiva.current_weather.ui.CurrentWeatherFragment
import com.mukiva.openweather.core.di.FeatureScope
import com.mukiva.openweather.core.di.MultiViewModelFactory
import dagger.Component

@[FeatureScope Component(
    modules = [
        CurrentWeatherModule::class,
        CurrentWeatherBinds::class
              ],
    dependencies = [CurrentWeatherDependencies::class]
)]
internal interface CurrentWeatherComponent {

    val factory: MultiViewModelFactory

    fun inject(fragment: CurrentWeatherFragment)

    companion object {

        private var mInstance: CurrentWeatherComponent? = null

        private fun init(): CurrentWeatherComponent {
            mInstance = DaggerCurrentWeatherComponent.builder()
                .currentWeatherDependencies(CurrentWeatherDepsProvider.deps)
                .build()
            return mInstance!!
        }

        fun get(): CurrentWeatherComponent {
            return mInstance ?: init()
        }
    }
}






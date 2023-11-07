package com.mukiva.current_weather.di

import kotlin.properties.Delegates

object CurrentWeatherDepsStore : CurrentWeatherDepsProvider {
    override var deps: CurrentWeatherDependencies by Delegates.notNull()
}
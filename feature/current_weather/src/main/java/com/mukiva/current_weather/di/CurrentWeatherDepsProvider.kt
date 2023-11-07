package com.mukiva.current_weather.di

interface CurrentWeatherDepsProvider {
    var deps: CurrentWeatherDependencies

    companion object : CurrentWeatherDepsProvider by CurrentWeatherDepsStore
}
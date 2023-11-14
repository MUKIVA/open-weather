package com.mukiva.current_weather.navigation

import androidx.fragment.app.Fragment
import com.mukiva.core.navigation.IBaseScreen
import com.mukiva.current_weather.ui.CurrentWeatherFragment

class CurrentWeatherScreen : IBaseScreen {
    override val fragment: Fragment
        get() = CurrentWeatherFragment.newInstance()
}
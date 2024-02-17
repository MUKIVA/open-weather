package com.mukiva.openweather.glue.dashboard.ui

import androidx.fragment.app.Fragment
import com.mukiva.feature.dashboard.ui.IMinimalForecastFragmentFactory
import com.mukiva.feature.forecast.ui.DaysForecastMinimalFragment
import javax.inject.Inject

class MinimalForecastFragmentFactory @Inject constructor() : IMinimalForecastFragmentFactory {
    override fun createFragment(locationName: String): Fragment {
        return DaysForecastMinimalFragment.newInstance(locationName)
    }
}
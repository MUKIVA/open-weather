package com.mukiva.feature.forecast_impl

import androidx.fragment.app.Fragment
import com.mukiva.feature.forecast_api.IForecastFragmentProvider

class ForecastFragmentProvider : IForecastFragmentProvider {
    override fun provideMinimalFragment(location: String): Fragment =
        DaysForecastMinimalFragment.newInstance(location)
}
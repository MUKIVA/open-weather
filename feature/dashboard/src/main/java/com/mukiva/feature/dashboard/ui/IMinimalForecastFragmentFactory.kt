package com.mukiva.feature.dashboard.ui

import androidx.fragment.app.Fragment

interface IMinimalForecastFragmentFactory {
    fun createFragment(locationName: String): Fragment

}
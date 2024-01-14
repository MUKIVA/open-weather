package com.mukiva.feature.forecast_api

import androidx.fragment.app.Fragment


interface IForecastFragmentProvider {

    fun provideMinimalFragment(location: String): Fragment

}
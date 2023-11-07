package com.mukiva.location_search.di

import com.mukiva.location_search.domain.ILocationSearchGateway
import com.mukiva.location_search.presentation.SearchLocationState
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class LocationSearchModule {

    @[Provides]
    fun provideSearchLocationState() = SearchLocationState.default()

    @[Provides]
    fun provideSearchLocationGateway(): ILocationSearchGateway {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ILocationSearchGateway::class.java)
    }

}
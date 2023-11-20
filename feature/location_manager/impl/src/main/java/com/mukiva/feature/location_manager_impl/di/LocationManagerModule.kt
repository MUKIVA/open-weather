package com.mukiva.feature.location_manager_impl.di

import android.app.Application
import androidx.room.Room
import com.mukiva.feature.location_manager_impl.data.LocationDataBase
import com.mukiva.feature.location_manager_impl.data.LocationRepository
import com.mukiva.feature.location_manager_impl.domain.ILocationRepository
import com.mukiva.feature.location_manager_impl.domain.ILocationSearchGateway
import com.mukiva.feature.location_manager_impl.presentation.LocationManagerState
import com.mukiva.openweather.core.di.IApiKeyProvider
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class LocationManagerModule {

    @[Provides]
    fun provideSearchLocationState() = LocationManagerState.default()

    @[Provides]
    fun provideSearchLocationGateway(): ILocationSearchGateway {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ILocationSearchGateway::class.java)
    }

    @[Provides]
    fun provideLocationDataBase(application: Application): LocationDataBase {
        return Room.databaseBuilder(
            application,
            LocationDataBase::class.java,
            LOCATION_DB_NAME
            ).build()
    }

    @[Provides]
    fun provideLocationRepository(
        gateway: ILocationSearchGateway,
        store: LocationDataBase,
        apiKeyProvider: IApiKeyProvider
    ): ILocationRepository = LocationRepository(gateway, store, apiKeyProvider)

    companion object {
        private const val LOCATION_DB_NAME = "locationDb"
    }

}
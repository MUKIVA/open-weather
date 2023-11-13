package com.mukiva.location_search.di

import android.app.Application
import androidx.room.Room
import com.mukiva.location_search.data.LocationDataBase
import com.mukiva.location_search.data.LocationRepository
import com.mukiva.location_search.domain.ILocationRepository
import com.mukiva.location_search.domain.ILocationSearchGateway
import com.mukiva.location_search.presentation.SearchLocationState
import com.mukiva.openweather.core.di.IApiKeyProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IOCoroutineScope {
    suspend fun <T> asyncWithDispatcher(dispatcher: CoroutineDispatcher, block: suspend() -> T): T {
        return CoroutineScope(Job() + dispatcher).async {
            block()
        }.await()
    }
}

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

    @[Provides]
    fun provideIOCoroutineScope() = IOCoroutineScope()

    companion object {
        private const val LOCATION_DB_NAME = "locationDb"
    }

}
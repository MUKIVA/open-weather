package com.mukiva.feature.location_manager_impl.di

import android.content.Context
import androidx.room.Room
import com.mukiva.feature.location_manager_impl.data.LocationDataBase
import com.mukiva.feature.location_manager_impl.domain.ILocationSearchGateway
import com.mukiva.feature.location_manager_impl.presentation.LocationManagerState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class LocationManagerModule {

    @Provides
    fun provideLocationSearchGateway(
        retrofit: Retrofit
    ): ILocationSearchGateway {
        return retrofit.create(ILocationSearchGateway::class.java)
    }

    @Provides
    fun provideLocationStore(
        @ApplicationContext context: Context
    ): LocationDataBase {
        return Room.databaseBuilder(
            context,
            LocationDataBase::class.java, "location-db"
        ).build()
    }

    @Provides
    fun provideLocationManagerState() = LocationManagerState.default()

}
package com.mukiva.core.data.repository.location.di

import android.content.Context
import androidx.room.Room
import com.mukiva.core.data.repository.location.database.LocationDataBase
import com.mukiva.core.data.repository.location.gateway.ILocationSearchGateway
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class LocationManagerDataModule {
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

}
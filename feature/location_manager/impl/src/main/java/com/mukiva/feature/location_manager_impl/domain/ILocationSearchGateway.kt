package com.mukiva.feature.location_manager_impl.domain

import com.mukiva.feature.location_manager_impl.data.LocationRemoteEntity
import com.mukiva.openweather.data.IGateway
import retrofit2.http.GET
import retrofit2.http.Query

interface ILocationSearchGateway : IGateway {
    @GET("search.json")
    suspend fun searchLocations(
        @Query("key") key: String,
        @Query("q") q: String,
    ) : List<LocationRemoteEntity>
}
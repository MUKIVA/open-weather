package com.mukiva.feature.location_manager_impl.domain

import com.mukiva.feature.location_manager_impl.data.LocationRemoteEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface ILocationSearchGateway {
    @GET("search.json")
    suspend fun searchLocations(
        @Query("key") key: String,
        @Query("q") q: String,
    ) : List<LocationRemoteEntity>
}
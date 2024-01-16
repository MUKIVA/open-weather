package com.mukiva.core.data.repository.location.gateway

import com.mukiva.core.data.entity.LocationRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface ILocationSearchGateway {
    @GET("search.json")
    suspend fun searchLocations(
        @Query("key") key: String,
        @Query("q") q: String,
    ) : List<LocationRemote>
}
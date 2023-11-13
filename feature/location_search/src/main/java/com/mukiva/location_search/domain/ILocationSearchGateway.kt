package com.mukiva.location_search.domain

import com.mukiva.location_search.data.LocationRemoteEntity
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
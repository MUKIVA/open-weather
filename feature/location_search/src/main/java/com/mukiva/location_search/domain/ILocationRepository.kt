package com.mukiva.location_search.domain

import com.mukiva.location_search.data.LocationLocalEntity
import com.mukiva.location_search.data.LocationRemoteEntity

interface ILocationRepository {
    suspend fun searchRemote(q: String): List<LocationRemoteEntity>
    suspend fun getAllLocal(): List<LocationLocalEntity>
    suspend fun getLocalById(locationId: Int): LocationLocalEntity?
    suspend fun addLocalLocation(vararg location: LocationLocalEntity)
    suspend fun removeLocalLocation(location: LocationLocalEntity)
}
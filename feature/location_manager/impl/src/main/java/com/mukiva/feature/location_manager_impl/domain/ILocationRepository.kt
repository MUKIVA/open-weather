package com.mukiva.feature.location_manager_impl.domain

import com.mukiva.feature.location_manager_impl.data.LocationLocalEntity
import com.mukiva.feature.location_manager_impl.data.LocationRemoteEntity


interface ILocationRepository {
    suspend fun searchRemote(q: String): List<LocationRemoteEntity>
    suspend fun getAllLocal(): List<LocationLocalEntity>
    suspend fun getLocalById(locationId: Int): LocationLocalEntity?
    suspend fun addLocalLocation(vararg location: LocationLocalEntity)
    suspend fun removeLocalLocation(location: LocationLocalEntity)
}
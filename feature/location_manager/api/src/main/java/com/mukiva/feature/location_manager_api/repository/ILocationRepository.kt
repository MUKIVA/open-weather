package com.mukiva.feature.location_manager_api.repository

import com.mukiva.feature.location_manager_api.dto.LocationDTO


interface ILocationRepository {
    suspend fun searchRemote(q: String): List<LocationDTO>
    suspend fun getAllLocal(): List<LocationDTO>
    suspend fun getLocalById(locationId: Int): LocationDTO?
    suspend fun addLocalLocation(vararg location: LocationDTO)
    suspend fun removeLocalLocation(location: LocationDTO)
    suspend fun removeAllLocalLocations()
}
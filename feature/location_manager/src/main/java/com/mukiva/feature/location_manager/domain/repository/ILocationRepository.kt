package com.mukiva.feature.location_manager.domain.repository

import com.mukiva.feature.location_manager.domain.model.Location

interface ILocationRepository {

    suspend fun searchRemote(q: String): List<Location>
    suspend fun getAllLocal(): List<Location>
    suspend fun getLocalById(locationId: Int): Location?
    suspend fun addLocalLocation(vararg location: Location)
    suspend fun removeLocalLocation(location: Location)
    suspend fun removeAllLocalLocations()

}
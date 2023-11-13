package com.mukiva.location_search.data

import com.mukiva.location_search.domain.ILocationRepository
import com.mukiva.location_search.domain.ILocationSearchGateway
import com.mukiva.openweather.core.di.IApiKeyProvider
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val gateway: ILocationSearchGateway,
    private val store: LocationDataBase,
    private val apiKeyProvider: IApiKeyProvider
) : ILocationRepository {
    override suspend fun searchRemote(q: String): List<LocationRemoteEntity> {
        return gateway.searchLocations(
            key = apiKeyProvider.apiKey,
            q = q
        )
    }

    override suspend fun getAllLocal(): List<LocationLocalEntity> {
        return store.locationDao().getAll()
    }

    override suspend fun getLocalById(locationId: Int): LocationLocalEntity? {
        return store.locationDao().getById(locationId)
    }

    override suspend fun addLocalLocation(vararg location: LocationLocalEntity) {
        return store.locationDao().insert(*location)
    }

    override suspend fun removeLocalLocation(location: LocationLocalEntity) {
        return store.locationDao().delete(location)
    }

}
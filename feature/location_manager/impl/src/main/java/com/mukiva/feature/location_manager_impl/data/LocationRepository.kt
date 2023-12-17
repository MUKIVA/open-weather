package com.mukiva.feature.location_manager_impl.data

import com.mukiva.feature.location_manager_api.dto.LocationDTO
import com.mukiva.feature.location_manager_api.repository.ILocationRepository
import com.mukiva.feature.location_manager_impl.domain.ILocationSearchGateway
import com.mukiva.openweather.core.di.IApiKeyProvider
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val gateway: ILocationSearchGateway,
    private val store: LocationDataBase,
    private val apiKeyProvider: IApiKeyProvider
) : ILocationRepository {
    override suspend fun searchRemote(q: String): List<LocationDTO> {
        return gateway.searchLocations(
            key = apiKeyProvider.apiKey,
            q = q
        ).map { it.asDTO() }
    }

    override suspend fun getAllLocal(): List<LocationDTO> {
        return store.locationDao()
            .getAll()
            .map { it.asDTO() }
    }

    override suspend fun getLocalById(locationId: Int): LocationDTO? {
        return store.locationDao()
            .getById(locationId)
            ?.asDTO()
    }

    override suspend fun addLocalLocation(vararg location: LocationDTO) {

        val mappedLocations = location
            .map { it.asLocal() }
            .toTypedArray()

        return store.locationDao()
            .insert(*mappedLocations)
    }

    override suspend fun removeLocalLocation(location: LocationDTO) {
        return store.locationDao().delete(location.asLocal())
    }

    private fun LocationLocalEntity.asDTO() = LocationDTO(
        uid = uid,
        cityName = cityName,
        regionName = regionName,
        lon = lon,
        lat = lat,
        countryName = countryName
    )

    private fun LocationRemoteEntity.asDTO() = LocationDTO(
        uid = id ?: 0,
        cityName = name,
        regionName = region,
        lon = lon,
        lat = lat,
        countryName = country
    )

    private fun LocationDTO.asLocal() = LocationLocalEntity(
        uid = uid,
        cityName = cityName,
        regionName = regionName,
        lon = lon,
        lat = lat,
        countryName = countryName
    )

}
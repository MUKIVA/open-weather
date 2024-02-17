package com.mukiva.core.data.repository.location.impl

import com.mukiva.core.data.entity.LocationRemote
import com.mukiva.core.data.repository.location.ILocationRepository
import com.mukiva.core.data.repository.location.database.LocationDataBase
import com.mukiva.core.data.repository.location.entity.LocationDTO
import com.mukiva.core.data.repository.location.entity.LocationLocalEntity
import com.mukiva.core.data.repository.location.gateway.ILocationSearchGateway
import com.mukiva.core.network.IApiKeyProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val gateway: ILocationSearchGateway,
    private val dataBase: LocationDataBase,
    private val apiKeyProvider: IApiKeyProvider
) : ILocationRepository {
    override suspend fun searchRemote(q: String): List<LocationDTO> {
        return gateway.searchLocations(
            key = apiKeyProvider.apiKey,
            q = q
        ).mapIndexed { index, locationRemoteEntity ->
            locationRemoteEntity.asDTO(index)
        }
    }

    override suspend fun getAllLocal(): List<LocationDTO> {
        return dataBase.locationDao()
            .getAll()
            .map { it.asDTO() }
    }

    override suspend fun getLocalById(locationId: Int): LocationDTO? {
        return dataBase.locationDao()
            .getById(locationId)
            ?.asDTO()
    }

    override suspend fun addLocalLocation(vararg location: LocationDTO) {

        val mappedLocations = location
            .map { it.asLocal() }
            .toTypedArray()

        return dataBase.locationDao()
            .insert(*mappedLocations)
    }

    override suspend fun removeLocalLocation(location: LocationDTO) {
        return dataBase.locationDao().delete(
            location.asLocal()
        )
    }

    override suspend fun removeAllLocalLocations() {
        dataBase.locationDao().deleteAll()
    }

    private fun LocationLocalEntity.asDTO() = LocationDTO(
        uid = uid,
        position = position,
        cityName = cityName,
        regionName = regionName,
        lon = lon,
        lat = lat,
        countryName = countryName
    )

    private fun LocationRemote.asDTO(
        pos: Int
    ) = LocationDTO(
        uid = id,
        position = pos,
        cityName = name,
        regionName = region,
        lon = lon,
        lat = lat,
        countryName = country
    )

    private fun LocationDTO.asLocal() = LocationLocalEntity(
        uid = uid,
        position = position,
        cityName = cityName,
        regionName = regionName,
        lon = lon,
        lat = lat,
        countryName = countryName
    )

}
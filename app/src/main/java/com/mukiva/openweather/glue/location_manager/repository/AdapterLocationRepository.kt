//package com.mukiva.openweather.glue.location_manager.repository
//
//import com.mukiva.feature.location_manager.domain.model.Location
//import com.mukiva.feature.location_manager.domain.repository.ILocationRepository
//import com.mukiva.openweather.glue.location_manager.mapper.LocationMapper
//import com.mukiva.core.data.repository.location.ILocationRepository as ICoreLocationRepository
//import javax.inject.Inject
//
//class AdapterLocationRepository @Inject constructor(
//    private val coreRepository: ICoreLocationRepository
//) : ILocationRepository {
//    override suspend fun searchRemote(q: String): List<Location> {
//        return coreRepository.searchRemote(q).map(LocationMapper::asDomain)
//    }
//
//    override suspend fun getAllLocal(): List<Location> {
//        return coreRepository.getAllLocal().map(LocationMapper::asDomain)
//    }
//
//    override suspend fun getLocalById(locationId: Int): Location? {
//        return coreRepository.getLocalById(locationId)?.let {
//            LocationMapper.asDomain(it)
//        }
//    }
//
//    override suspend fun addLocalLocation(vararg location: Location) {
//        val typedArray = location
//            .map(LocationMapper::asDTO)
//            .toTypedArray()
//
//        coreRepository.addLocalLocation(*typedArray)
//    }
//
//    override suspend fun removeLocalLocation(location: Location) {
//        coreRepository.removeLocalLocation(LocationMapper.asDTO(location))
//    }
//
//    override suspend fun removeAllLocalLocations() {
//        coreRepository.removeAllLocalLocations()
//    }
//
//}
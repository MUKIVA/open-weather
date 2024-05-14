package com.github.mukiva.feature.locationmanager.domain.usecase

import com.github.mukiva.feature.locationmanager.domain.model.Location
import com.github.mukiva.weatherdata.LocationRepository
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.github.mukiva.weatherdata.models.Location as DataLocation

class LocationSearchUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    operator fun invoke(q: String): Flow<RequestResult<List<Location>>> {
        val local = repository.getAllLocal()
        val remote = repository.searchRemote(q)
        return local.combine(remote, ::mergeStrategy)
            .map { requestResult ->
                requestResult.map { locations ->
                    locations.map(::toLocation)
                }
            }
    }

    private fun toLocation(dataLocation: DataLocation): Location {
        return Location(
            id = dataLocation.id.toInt(),
            position = dataLocation.priority,
            cityName = dataLocation.name,
            regionName = dataLocation.region,
            countryName = dataLocation.country,
            isAdded = false
        )
    }

    private fun mergeStrategy(
        local: RequestResult<List<DataLocation>>,
        remote: RequestResult<List<DataLocation>>,
    ): RequestResult<List<DataLocation>> {
        return when {
            local is RequestResult.InProgress && remote is RequestResult.InProgress ->
                RequestResult.InProgress(data = null)
            local is RequestResult.Success && remote is RequestResult.Success ->
                mergeStrategy(local, remote)
            local is RequestResult.InProgress && remote is RequestResult.Error ->
                mergeStrategy(remote)
            local is RequestResult.Success && remote is RequestResult.Error ->
                mergeStrategy(remote)
            local is RequestResult.Error && remote is RequestResult.Success ->
                mergeStrategy(local)
            local is RequestResult.Error && remote is RequestResult.Error ->
                mergeStrategy(local)
            else -> mergeStrategy()
        }
    }

    private fun mergeStrategy(
        local: RequestResult.Success<List<DataLocation>>,
        remote: RequestResult.Success<List<DataLocation>>,
    ): RequestResult<List<DataLocation>> {
        val localIds = checkNotNull(local.data).map { it.id }
        val resultData = checkNotNull(remote.data).filterNot { remoteLocation ->
            remoteLocation.id in localIds
        }
        return RequestResult.Success(data = resultData)
    }

    private fun mergeStrategy(
        source: RequestResult.Error<List<DataLocation>>,
    ): RequestResult<List<DataLocation>> {
        return RequestResult.Error(data = null, error = source.error)
    }

    private fun mergeStrategy(): RequestResult<List<DataLocation>> {
        return RequestResult.InProgress(data = null)
    }
}

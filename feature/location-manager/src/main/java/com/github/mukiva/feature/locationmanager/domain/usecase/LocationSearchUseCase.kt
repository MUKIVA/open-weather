package com.github.mukiva.feature.locationmanager.domain.usecase

import com.github.mukiva.feature.locationmanager.domain.model.Location
import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.ISettingsRepository
import com.github.mukiva.weatherdata.utils.RequestResult
import com.github.mukiva.weatherdata.utils.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.github.mukiva.weatherdata.models.LocationData as DataLocation

internal class LocationSearchUseCase @Inject constructor(
    private val repository: ILocationRepository,
    private val settingsRepository: ISettingsRepository,
) {
    suspend operator fun invoke(q: String): Flow<RequestResult<List<Location>>> {
        val lang = settingsRepository
            .getLocalization()
            .flowOn(Dispatchers.Default)
            .first()
        val local = repository.getAllLocal()
            .flowOn(Dispatchers.Default)
        val remote = repository.searchRemote(q, lang)
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
        return RequestResult.Error(data = null, cause = source.cause)
    }

    private fun mergeStrategy(): RequestResult<List<DataLocation>> {
        return RequestResult.InProgress(data = null)
    }
}

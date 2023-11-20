package com.mukiva.feature.location_manager_impl.domain.usecase

import android.util.Log
import com.mukiva.feature.location_manager_impl.data.LocationRemoteEntity
import com.mukiva.feature.location_manager_impl.domain.ILocationRepository
import com.mukiva.feature.location_manager_impl.domain.mapper.LocationMapper
import com.mukiva.feature.location_manager_impl.domain.model.Location
import com.mukiva.openweather.core.di.IConnectionProvider
import com.mukiva.usecase.ApiError
import com.mukiva.usecase.ApiResult
import com.mukiva.usecase.CoroutineHelper
import java.text.ParseException
import javax.inject.Inject

class LocationSearchUseCase @Inject constructor(
    private val connectionProvider: IConnectionProvider,
    private val repository: ILocationRepository
) {

    suspend operator fun invoke(q: String): ApiResult<List<Location>> {
        if (!connectionProvider.hasConnection()) {
            return ApiResult.Error(ApiError.NO_INTERNET)
        }
        return try {
            CoroutineHelper.doIO {
                val data = repository.searchRemote(q)
                ApiResult.Success(
                    with (LocationMapper) { data.map<LocationRemoteEntity, Location> {
                        val local = it.id?.let { id ->  repository.getLocalById(id) }
                        it.asDomain(local)
                    }.filter { !it.isAdded }}
                )
            }
        } catch (e: Exception) {
            Log.d("SearchUseCase", "FAIL: ${e.message}")
            when(e) {
                is ParseException -> ApiResult.Error(ApiError.PARSE_ERROR)
                else -> ApiResult.Error(ApiError.SERVER_ERROR)
            }
        }
    }

}
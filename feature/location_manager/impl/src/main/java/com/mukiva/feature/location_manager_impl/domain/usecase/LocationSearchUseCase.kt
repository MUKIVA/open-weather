package com.mukiva.feature.location_manager_impl.domain.usecase

import android.util.Log
import com.mukiva.core.network.IConnectionProvider
import com.mukiva.feature.location_manager_api.repository.ILocationRepository
import com.mukiva.feature.location_manager_impl.domain.mapper.LocationMapper
import com.mukiva.feature.location_manager_impl.domain.model.Location
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
                val local = repository.getAllLocal()
                val result = data.filterNot { it in local }
                ApiResult.Success(
                    with (LocationMapper) {
                        result.map { it.asDomain() }
                    }
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
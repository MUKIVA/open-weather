package com.mukiva.feature.location_manager_impl.domain.usecase

import android.util.Log
import com.mukiva.feature.location_manager_api.repository.ILocationRepository
import com.mukiva.feature.location_manager_impl.domain.mapper.LocationMapper
import com.mukiva.feature.location_manager_impl.domain.model.Location
import com.mukiva.usecase.ApiError
import com.mukiva.usecase.ApiResult
import com.mukiva.usecase.CoroutineHelper
import java.text.ParseException
import javax.inject.Inject

class GetAddedLocationsUseCase @Inject constructor(
    private val repository: ILocationRepository
) {

    suspend operator fun invoke(): ApiResult<List<Location>> {
        return CoroutineHelper.doIO {
            wrapResult {
                with(LocationMapper) {
                    repository.getAllLocal().map {
                        it.asDomain()
                    }.sortedBy { it.position }
                }
            }
        }
    }

    private suspend fun <TResult> wrapResult(
        block: suspend() -> TResult
    ): ApiResult<TResult> {
        return try {
            val result = block()
            ApiResult.Success(result)
        } catch (e: Exception) {
            Log.d("GetAddedLocationsUseCase", "${e.message}")
            when (e) {
                is ParseException -> ApiResult.Error(ApiError.PARSE_ERROR)
                else -> ApiResult.Error(ApiError.SERVER_ERROR)
            }
        }
    }

}
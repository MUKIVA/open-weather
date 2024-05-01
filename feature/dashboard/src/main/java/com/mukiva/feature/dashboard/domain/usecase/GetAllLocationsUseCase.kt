package com.mukiva.feature.dashboard.domain.usecase

import android.util.Log
import com.mukiva.feature.dashboard.domain.model.Location
import com.mukiva.feature.dashboard.domain.repository.ILocationRepository
import com.mukiva.usecase.ApiError
import com.mukiva.usecase.ApiResult
import com.mukiva.usecase.CoroutineHelper
import javax.inject.Inject

class GetAllLocationsUseCase @Inject constructor(
    private val locationRepository: ILocationRepository
) {

    suspend operator fun invoke(): ApiResult<List<Location>> {
        return try {
            CoroutineHelper.doIO {
                ApiResult.Success(locationRepository
                    .getAllLocations())
            }
        } catch (e: Exception) {
            Log.d("GetAllLocations", "${e.message}")
            ApiResult.Error(ApiError.PARSE_ERROR)
        }
    }

}
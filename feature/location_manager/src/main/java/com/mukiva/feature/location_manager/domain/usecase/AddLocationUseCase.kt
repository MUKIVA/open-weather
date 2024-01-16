package com.mukiva.feature.location_manager.domain.usecase

import android.util.Log
import com.mukiva.core.network.IConnectionProvider
import com.mukiva.feature.location_manager.domain.model.Location
import com.mukiva.feature.location_manager.domain.repository.ILocationRepository
import com.mukiva.usecase.ApiError
import com.mukiva.usecase.ApiResult
import com.mukiva.usecase.CoroutineHelper
import java.text.ParseException
import javax.inject.Inject

class AddLocationUseCase @Inject constructor(
    private val connectionProvider: IConnectionProvider,
    private val repository: ILocationRepository
) {
    suspend operator fun invoke(location: Location): ApiResult<Unit> {
        if (!connectionProvider.hasConnection()) {
            return ApiResult.Error(ApiError.NO_INTERNET)
        }

        return try {
            CoroutineHelper.doIO {
                repository.addLocalLocation(location)
            }
            ApiResult.Success(Unit)
        } catch (err: Exception) {
            Log.d("AddLocationUseCase", "${err.message}")
            when (err) {
                is ParseException -> ApiResult.Error(ApiError.PARSE_ERROR)
                else -> ApiResult.Error(ApiError.SERVER_ERROR)
            }
        }
    }

}
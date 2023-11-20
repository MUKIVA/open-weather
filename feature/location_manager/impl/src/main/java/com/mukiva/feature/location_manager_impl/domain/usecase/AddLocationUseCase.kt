package com.mukiva.feature.location_manager_impl.domain.usecase

import android.util.Log
import com.mukiva.feature.location_manager_impl.domain.ILocationRepository
import com.mukiva.feature.location_manager_impl.domain.mapper.LocationMapper
import com.mukiva.feature.location_manager_impl.domain.model.Location
import com.mukiva.openweather.core.di.IConnectionProvider
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
                repository.addLocalLocation(with(LocationMapper) {
                    location.asLocalEntity()
                })
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
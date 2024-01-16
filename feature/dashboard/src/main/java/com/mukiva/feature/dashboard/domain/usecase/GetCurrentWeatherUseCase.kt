package com.mukiva.feature.dashboard.domain.usecase

import android.util.Log
import com.mukiva.core.network.IConnectionProvider
import com.mukiva.feature.dashboard.domain.model.CurrentWithLocation
import com.mukiva.feature.dashboard.domain.repository.ICurrentWeatherRepository
import com.mukiva.usecase.ApiError
import com.mukiva.usecase.ApiResult
import java.text.ParseException
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val currentWeatherRepository: ICurrentWeatherRepository,
    private val connectionProvider: IConnectionProvider
) {

    suspend operator fun invoke(location: String): ApiResult<CurrentWithLocation> {
        if (!connectionProvider.hasConnection()) {
            return ApiResult.Error(ApiError.NO_INTERNET)
        }
        return try {
            ApiResult.Success(currentWeatherRepository
                .getCurrent(location))
        } catch (e: Exception) {
            Log.d("GetCurrentWeatherUseCase", "FAIL: ${e.message}")
            return when(e) {
                is ParseException -> ApiResult.Error(ApiError.PARSE_ERROR)
                else -> ApiResult.Error(ApiError.SERVER_ERROR)
            }
        }
    }

}
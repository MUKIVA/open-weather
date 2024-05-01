package com.mukiva.feature.forecast.domain.usecase

import android.util.Log
import com.mukiva.core.network.IConnectionProvider
import com.mukiva.feature.forecast.presentation.HourlyForecast
import com.mukiva.feature.forecast.domain.repository.IForecastRepository
import com.mukiva.usecase.ApiError
import com.mukiva.usecase.ApiResult
import javax.inject.Inject

class GetFullForecastUseCase @Inject constructor(
    private val connectionStatusProvider: IConnectionProvider,
    private val forecastRepo: IForecastRepository
) {


    suspend operator fun invoke(locationName: String, days: Int): ApiResult<List<HourlyForecast>> {
        if (!connectionStatusProvider.hasConnection()) {
            return ApiResult.Error(ApiError.NO_INTERNET)
        }

        return try {
            ApiResult.Success(forecastRepo
                .getFullForecast(locationName, days)
            )
        } catch (e: Exception) {
            Log.d("GetFullForecastUseCase", "${e.message}")
            ApiResult.Error(ApiError.PARSE_ERROR)
        }
    }

}
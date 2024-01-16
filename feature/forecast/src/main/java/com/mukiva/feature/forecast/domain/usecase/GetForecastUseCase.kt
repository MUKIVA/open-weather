package com.mukiva.feature.forecast.domain.usecase

import android.util.Log
import com.mukiva.core.network.IConnectionProvider
import com.mukiva.feature.forecast.domain.Forecast
import com.mukiva.feature.forecast.domain.repository.IForecastRepository
import com.mukiva.usecase.ApiError
import com.mukiva.usecase.ApiResult
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val connectionProvider: IConnectionProvider,
    private val forecastRepository: IForecastRepository,
) {

    suspend operator fun invoke(locationName: String): ApiResult<List<Forecast>> {
        if (!connectionProvider.hasConnection()) {
            return ApiResult.Error(ApiError.NO_INTERNET)
        }

        return try {

            val data = forecastRepository.getForecast(
                locationName = locationName,
                days = 3
            )

            ApiResult.Success(data)
        } catch (e: Exception) {
            Log.d("GetForecastUseCase", "${e.message}")
            ApiResult.Error(ApiError.PARSE_ERROR)
        }
    }
}
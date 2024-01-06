package com.mukiva.feature.dashboard_impl.domain.usecase

import android.util.Log
import com.mukiva.core.network.IApiKeyProvider
import com.mukiva.core.network.IConnectionProvider
import com.mukiva.feature.dashboard_impl.domain.gateway.ICurrentWeatherGateway
import com.mukiva.feature.dashboard_impl.domain.mapper.CurrentWithLocationMapper
import com.mukiva.feature.dashboard_impl.domain.model.CurrentWithLocation
import com.mukiva.usecase.ApiError
import com.mukiva.usecase.ApiResult
import java.text.ParseException
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val gateway: ICurrentWeatherGateway,
    private val mapper: CurrentWithLocationMapper,
    private val apiKeyProvider: IApiKeyProvider,
    private val connectionProvider: IConnectionProvider
) {

    suspend operator fun invoke(location: String): ApiResult<CurrentWithLocation> {
        if (!connectionProvider.hasConnection()) {
            return ApiResult.Error(ApiError.NO_INTERNET)
        }
        return try {
            val data = gateway.getCurrentWeather(
                key = apiKeyProvider.apiKey,
                q = location,
                aqi = "No"
            )
            ApiResult.Success(
                with(mapper) { data.mapToDomain() }
            )
        } catch (e: Exception) {
            Log.d("GetCurrentWeatherUseCase", "FAIL: ${e.message}")
            return when(e) {
                is ParseException -> ApiResult.Error(ApiError.PARSE_ERROR)
                else -> ApiResult.Error(ApiError.SERVER_ERROR)
            }
        }
    }

}
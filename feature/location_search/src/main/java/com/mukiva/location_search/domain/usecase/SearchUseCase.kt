package com.mukiva.location_search.domain.usecase

import android.util.Log
import com.mukiva.location_search.domain.ILocationSearchGateway
import com.mukiva.location_search.domain.LocationPoint
import com.mukiva.location_search.domain.LocationPointMapper
import com.mukiva.openweather.core.di.IApiKeyProvider
import com.mukiva.openweather.core.di.IConnectionProvider
import com.mukiva.usecase.ApiError
import com.mukiva.usecase.ApiResult
import java.text.ParseException
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val apiKeyProvider: IApiKeyProvider,
    private val connectionProvider: IConnectionProvider,
    private val locationSearchGateway: ILocationSearchGateway,
    private val locationMapper: LocationPointMapper
) {

    suspend operator fun invoke(q: String): ApiResult<List<LocationPoint>> {
        if (!connectionProvider.hasConnection()) {
            return ApiResult.Error(ApiError.NO_INTERNET)
        }
        return try {
            val data = locationSearchGateway.getLocations(
                key = apiKeyProvider.apiKey,
                q = q
            )
            ApiResult.Success(
                with (locationMapper) { data.map { it.toDomain() } }
            )
        } catch (e: Exception) {
            Log.d("SearchUseCase", "FAIL: ${e.message}")
            when(e) {
                is ParseException -> ApiResult.Error(ApiError.PARSE_ERROR)
                else -> ApiResult.Error(ApiError.SERVER_ERROR)
            }
        }
    }

}
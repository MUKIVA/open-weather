package com.domain.usecase

import android.util.Log
import com.domain.model.Location
import com.mukiva.feature.location_manager_api.dto.LocationDTO
import com.mukiva.feature.location_manager_api.repository.ILocationRepository
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
                val locations = locationRepository.getAllLocal()
                val parsedLocations = locations
                    .sortedBy { it.position }
                    .map { it.asDomain() }

                ApiResult.Success(parsedLocations)
            }
        } catch (e: Exception) {
            Log.d("GetAllLocations", "${e.message}")
            ApiResult.Error(ApiError.PARSE_ERROR)
        }
    }


    private fun LocationDTO.asDomain() = Location(
        name = cityName ?: "",
        region = regionName ?: "",
        country = countryName ?: "",
        lat = lat?.toFloat() ?: 0.0f,
        lon = lon?.toFloat() ?: 0.0f
    )

}
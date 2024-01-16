package com.mukiva.feature.location_manager.domain.usecase

import android.util.Log
import com.mukiva.feature.location_manager.domain.repository.ILocationRepository
import com.mukiva.feature.location_manager.presentation.EditableLocation
import com.mukiva.usecase.ApiError
import com.mukiva.usecase.ApiResult
import com.mukiva.usecase.CoroutineHelper
import java.text.ParseException
import javax.inject.Inject

class UpdateStoredLocationsUseCase @Inject constructor(
    private val repository: ILocationRepository
) {

    suspend operator fun invoke(newList: List<EditableLocation>): ApiResult<Unit> {
        return try {
            CoroutineHelper.doIO {

                val typedArray = newList.mapIndexed { index, item ->
                    item.location.copy(position = index)
                }.toTypedArray()

                repository.removeAllLocalLocations()
                repository.addLocalLocation(*typedArray)
                ApiResult.Success(Unit)
            }
        } catch (e: Exception) {
            Log.d("UpdateStoredLocationsUseCase", "FAIL: ${e.message}")
            when (e) {
                is ParseException -> ApiResult.Error(ApiError.PARSE_ERROR)
                else -> ApiResult.Error(ApiError.SERVER_ERROR)
            }
        }
    }
}
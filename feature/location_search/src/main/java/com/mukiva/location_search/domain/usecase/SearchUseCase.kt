package com.mukiva.location_search.domain.usecase

import android.util.Log
import com.mukiva.location_search.di.IOCoroutineScope
import com.mukiva.location_search.domain.ILocationRepository
import com.mukiva.location_search.domain.model.Location
import com.mukiva.location_search.domain.mapper.LocationMapper
import com.mukiva.openweather.core.di.IConnectionProvider
import com.mukiva.usecase.ApiError
import com.mukiva.usecase.ApiResult
import kotlinx.coroutines.Dispatchers
import java.text.ParseException
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val connectionProvider: IConnectionProvider,
    private val repository: ILocationRepository,
    private val ioCoroutineScope: IOCoroutineScope
) {

    suspend operator fun invoke(q: String): ApiResult<List<Location>> {
        if (!connectionProvider.hasConnection()) {
            return ApiResult.Error(ApiError.NO_INTERNET)
        }
        return try {
            ioCoroutineScope.asyncWithDispatcher(Dispatchers.IO) {
                val data = repository.searchRemote(q)
                ApiResult.Success(
                    with (LocationMapper) { data.map {
                        val local = it.id?.let { id ->  repository.getLocalById(id) }
                        it.asDomain(local)
                    }.filter { !it.isAdded }}
                )
            }
        } catch (e: Exception) {
            Log.d("SearchUseCase", "FAIL: ${e.message}")
            when(e) {
                is ParseException -> ApiResult.Error(ApiError.PARSE_ERROR)
                else -> ApiResult.Error(ApiError.SERVER_ERROR)
            }
        }
    }

}
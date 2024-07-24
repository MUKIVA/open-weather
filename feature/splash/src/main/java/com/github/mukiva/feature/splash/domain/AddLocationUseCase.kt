package com.github.mukiva.feature.splash.domain

import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.ISettingsRepository
import com.github.mukiva.weatherdata.models.LocationData
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class AddLocationUseCase @Inject constructor(
    private val locationRepository: ILocationRepository,
    private val locationProvider: ILocationProvider,
    private val settingsRepository: ISettingsRepository
) {
    suspend operator fun invoke() {
        val lang = settingsRepository.getLocalization()
            .first()
        val location = locationProvider.getCurrentLocation() ?: return
        locationRepository.searchRemote(location.longitude, location.latitude, lang)
            .filter { requestResult -> requestResult !is RequestResult.InProgress }
            .onEach { requestResult ->
                if (requestResult is RequestResult.Success) {
                    saveFirstLocation(requestResult.data)
                }
            }
            .first()
    }

    private suspend fun saveFirstLocation(locationDataList: List<LocationData>?) {
        if (locationDataList.isNullOrEmpty()) return
        val location = locationDataList.single()
        locationRepository.addLocalLocation(location)
    }
}

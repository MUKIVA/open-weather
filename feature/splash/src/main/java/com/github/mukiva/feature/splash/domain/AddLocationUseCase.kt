package com.github.mukiva.feature.splash.domain

import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.ISettingsRepository
import com.github.mukiva.weatherdata.models.Location
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AddLocationUseCase @Inject constructor(
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

    private suspend fun saveFirstLocation(locationList: List<Location>?) {
        if (locationList.isNullOrEmpty()) return
        val location = locationList.single()
        locationRepository.addLocalLocation(location)
    }
}

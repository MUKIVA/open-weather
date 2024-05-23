package com.github.mukiva.feature.splash.domain

import com.github.mukiva.weatherdata.LocationRepository
import com.github.mukiva.weatherdata.SettingsRepository
import com.github.mukiva.weatherdata.models.Location
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class AddLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val locationProvider: ILocationProvider,
    private val settingsRepository: SettingsRepository
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

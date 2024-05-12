package com.mukiva.openweather.glue.forecast.repository

import com.github.mukiva.open_weather.core.domain.UnitsType
import com.mukiva.feature.forecast.domain.repository.ISettingsRepository
import com.mukiva.openweather.glue.settings.repository.AdapterSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdapterSettingsRepository @Inject constructor(
    private val coreRepo: AdapterSettingsRepository
) : ISettingsRepository {
    override fun getUnitsTypeFlow(): Flow<UnitsType> {
        return coreRepo.asAppConfig()
            .map { it.unitsType }
    }
}
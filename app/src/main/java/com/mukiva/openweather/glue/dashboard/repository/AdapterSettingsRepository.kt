package com.mukiva.openweather.glue.dashboard.repository

import com.mukiva.feature.dashboard.domain.model.UnitsType
import com.mukiva.feature.dashboard.domain.repository.ISettingsRepository
import com.mukiva.openweather.glue.dashboard.mapper.UnitsTypeMapper
import com.mukiva.openweather.glue.settings.repository.AdapterSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdapterSettingsRepository @Inject constructor(
    private val coreRepo: AdapterSettingsRepository
) : ISettingsRepository {
    override fun getUnitsTypeFlow(): Flow<UnitsType> {
        return coreRepo.asAppConfig()
            .map { UnitsTypeMapper.map(it.unitsType) }
    }
}
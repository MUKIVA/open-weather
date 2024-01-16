package com.mukiva.openweather.glue.settings.repository

import com.mukiva.feature.settings.domain.config.AppConfig
import com.mukiva.feature.settings.domain.config.Theme
import com.mukiva.feature.settings.domain.config.UnitsType
import com.mukiva.feature.settings.domain.repository.IGeneralSettingsSetter
import com.mukiva.feature.settings.domain.repository.ISettingsRepository
import com.mukiva.core.data.repository.settings.ISettingsRepository as ICoreSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdapterSettingsRepository @Inject constructor(
    private val coreRepository: ICoreSettingsRepository
) : ISettingsRepository, IGeneralSettingsSetter {
    override suspend fun setTheme(theme: Theme) {
        coreRepository.setTheme(theme.ordinal)
    }

    override suspend fun setUnitsType(type: UnitsType) {
        coreRepository.setUnitsType(type.ordinal)
    }

    override fun asAppConfig(): Flow<AppConfig> {
        return coreRepository.asAppConfig()
            .map {
                AppConfig(
                    theme = Theme.entries[it.theme],
                    unitsType = UnitsType.entries[it.unitsType]
                )
            }
    }
}
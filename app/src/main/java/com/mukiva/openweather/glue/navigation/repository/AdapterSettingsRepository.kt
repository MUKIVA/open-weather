package com.mukiva.openweather.glue.navigation.repository

import com.mukiva.navigation.domain.repository.ISettingsRepository
import com.mukiva.navigation.domain.Theme
import com.mukiva.openweather.glue.navigation.ThemeMapper
import com.mukiva.openweather.glue.settings.repository.AdapterSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdapterSettingsRepository @Inject constructor(
    private val coreRepo: AdapterSettingsRepository
) : ISettingsRepository {
    override fun getThemeFlow(): Flow<Theme> {
        return coreRepo.asAppConfig()
            .map { ThemeMapper.map(it.theme) }
    }
}
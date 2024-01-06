package com.mukiva.feature.settings_api.repository

import com.mukiva.feature.settings_api.domain.AppConfig
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {

    fun asAppConfig(): Flow<AppConfig>

}
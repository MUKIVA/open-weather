package com.mukiva.feature.settings.domain.repository

import com.mukiva.feature.settings.domain.config.AppConfig
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {

    fun asAppConfig(): Flow<AppConfig>

}
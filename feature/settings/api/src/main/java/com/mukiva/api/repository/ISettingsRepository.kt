package com.mukiva.api.repository

import com.mukiva.api.domain.AppConfig
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {

    fun asAppConfig(): Flow<AppConfig>

}
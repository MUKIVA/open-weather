package com.mukiva.core.data.repository.settings

import com.mukiva.core.data.repository.settings.entity.IntAppConfig
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {

    fun asAppConfig(): Flow<IntAppConfig>
    suspend fun setTheme(theme: Int)
    suspend fun setUnitsType(type: Int)
    suspend fun setLastForecastUpdateTime(date: String)
    suspend fun getLastForecastUpdateTime(): String?

}
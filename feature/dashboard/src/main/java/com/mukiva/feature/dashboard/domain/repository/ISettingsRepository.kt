package com.mukiva.feature.dashboard.domain.repository

import com.github.mukiva.open_weather.core.domain.UnitsType
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    fun getUnitsTypeFlow(): Flow<UnitsType>

}
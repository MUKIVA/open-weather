package com.mukiva.feature.forecast.domain.repository

import com.mukiva.feature.forecast.domain.UnitsType
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    fun getUnitsTypeFlow(): Flow<UnitsType>

}
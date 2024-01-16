package com.mukiva.feature.dashboard.domain.repository

import com.mukiva.feature.dashboard.domain.model.UnitsType
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    fun getUnitsTypeFlow(): Flow<UnitsType>

}
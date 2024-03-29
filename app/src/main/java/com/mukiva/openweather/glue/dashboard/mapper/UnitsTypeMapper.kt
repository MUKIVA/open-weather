package com.mukiva.openweather.glue.dashboard.mapper

import com.mukiva.feature.settings.domain.config.UnitsType
import com.mukiva.feature.dashboard.domain.model.UnitsType as TargetUnitsType

object UnitsTypeMapper {

    fun map(coreItem: UnitsType) = when(coreItem) {
        UnitsType.METRIC -> TargetUnitsType.METRIC
        UnitsType.IMPERIAL -> TargetUnitsType.IMPERIAL
    }

}
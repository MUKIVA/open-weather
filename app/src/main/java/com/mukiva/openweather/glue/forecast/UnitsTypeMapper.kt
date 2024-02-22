package com.mukiva.openweather.glue.forecast

import com.mukiva.feature.settings.domain.config.UnitsType
import com.mukiva.feature.forecast.domain.UnitsType as TargetUnitsType

object UnitsTypeMapper {

    fun map(coreItem: UnitsType) = when(coreItem) {
        UnitsType.METRIC -> TargetUnitsType.METRIC
        UnitsType.IMPERIAL -> TargetUnitsType.IMPERIAL
    }

}
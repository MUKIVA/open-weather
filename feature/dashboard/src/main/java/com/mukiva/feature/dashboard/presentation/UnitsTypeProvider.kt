package com.mukiva.feature.dashboard.presentation

import com.mukiva.feature.dashboard.domain.model.UnitsType

data object UnitsTypeProvider : IDashboardState {

    private var mUnitsType: UnitsType = UnitsType.METRIC

    fun updateValue(unitsType: UnitsType) {
        mUnitsType = unitsType
    }

    override val unitsType: UnitsType
        get() = mUnitsType
}
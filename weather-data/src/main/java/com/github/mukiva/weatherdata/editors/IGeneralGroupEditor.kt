package com.github.mukiva.weatherdata.editors

import com.github.mukiva.openweather.core.domain.settings.Group
import com.github.mukiva.openweather.core.domain.settings.Lang
import com.github.mukiva.openweather.core.domain.settings.Theme
import com.github.mukiva.openweather.core.domain.settings.UnitsType
import kotlinx.coroutines.flow.Flow

interface IGeneralGroupEditor {
    suspend fun setTheme(theme: Theme)
    fun getTheme(): Flow<Theme>
    suspend fun setUnitsType(unitsType: UnitsType)
    fun getUnitsType(): Flow<UnitsType>
    suspend fun setLocalization(lang: Lang)
    fun getLocalization(): Flow<Lang>
    fun getGeneralGroup(): Flow<Group.General>
}
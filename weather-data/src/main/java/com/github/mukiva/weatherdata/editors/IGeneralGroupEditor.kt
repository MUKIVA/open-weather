package com.github.mukiva.weatherdata.editors

import com.github.mukiva.openweather.core.domain.settings.Group
import com.github.mukiva.openweather.core.domain.settings.Lang
import com.github.mukiva.openweather.core.domain.settings.Theme
import com.github.mukiva.openweather.core.domain.settings.UnitsType
import kotlinx.coroutines.flow.Flow

public interface IGeneralGroupEditor {
    public suspend fun setTheme(theme: Theme)
    public fun getTheme(): Flow<Theme>
    public suspend fun setUnitsType(unitsType: UnitsType)
    public fun getUnitsType(): Flow<UnitsType>
    public suspend fun setLocalization(lang: Lang)
    public fun getLocalization(): Flow<Lang>
    public fun getGeneralGroup(): Flow<Group.General>
}
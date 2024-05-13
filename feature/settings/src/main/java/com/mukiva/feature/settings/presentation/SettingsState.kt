package com.mukiva.feature.settings.presentation

import com.mukiva.feature.settings.domain.SettingItem
import kotlin.enums.EnumEntries
import kotlin.reflect.KClass

sealed class SettingsState {
    data object Init : SettingsState()
    data class Content(
        val list: List<SettingItem>,
        val bottomSheetState: BottomSheetState
    ) : SettingsState()
}

sealed class BottomSheetState {
    data object Hide : BottomSheetState()
    data class Show(
        val key: KClass<*>,
        val variants: EnumEntries<*>,
        val selectedPosition: Int,
    ) : BottomSheetState()
}
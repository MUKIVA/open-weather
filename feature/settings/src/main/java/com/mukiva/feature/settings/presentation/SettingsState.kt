package com.mukiva.feature.settings.presentation

import com.mukiva.feature.settings.domain.SettingItem

sealed class SettingsState {
    data object Init : SettingsState()
    data class Content(
        val list: List<SettingItem>,
        val bottomSheetState: BottomSheetState
    ) : SettingsState()
}

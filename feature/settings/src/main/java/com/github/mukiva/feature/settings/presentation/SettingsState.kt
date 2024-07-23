package com.github.mukiva.feature.settings.presentation

import com.github.mukiva.feature.settings.domain.SettingItem

internal sealed class SettingsState {
    data object Init : SettingsState()
    data class Content(
        val list: List<SettingItem>,
        val bottomSheetState: BottomSheetState
    ) : SettingsState()
}

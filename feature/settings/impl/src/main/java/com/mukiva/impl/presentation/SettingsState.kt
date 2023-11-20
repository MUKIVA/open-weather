package com.mukiva.impl.presentation

import com.mukiva.impl.domain.SettingItem

internal data class SettingsState(
    val settingsList: List<SettingItem>
) {
    companion object {
        fun default() = SettingsState(
            settingsList = emptyList()
        )
    }
}
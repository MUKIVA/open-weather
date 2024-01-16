package com.mukiva.feature.settings.presentation

import com.mukiva.feature.settings.domain.SettingItem

data class SettingsState(
    val settingsList: List<SettingItem>
) {
    companion object {
        fun default() = SettingsState(
            settingsList = emptyList()
        )
    }
}
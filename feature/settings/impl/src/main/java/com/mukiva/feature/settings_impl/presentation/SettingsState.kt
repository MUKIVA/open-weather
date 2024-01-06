package com.mukiva.feature.settings_impl.presentation

import com.mukiva.feature.settings_impl.domain.SettingItem

data class SettingsState(
    val settingsList: List<SettingItem>
) {
    companion object {
        fun default() = SettingsState(
            settingsList = emptyList()
        )
    }
}
package com.mukiva.feature.settings_impl.navigation

import com.mukiva.feature.settings_api.navigation.ISettingsScreenProvider
import com.mukiva.core.navigation.IBaseScreen
import javax.inject.Inject

class SettingsScreenProvider @Inject constructor() : ISettingsScreenProvider {
    override val screen: IBaseScreen
        get() = SettingsScreen()
}
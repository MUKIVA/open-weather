package com.mukiva.impl.navigation

import com.mukiva.api.navigation.ISettingsScreenProvider
import com.mukiva.core.navigation.IBaseScreen

class SettingsScreenProvider : ISettingsScreenProvider {
    override val screen: IBaseScreen
        get() = SettingsScreen()
}
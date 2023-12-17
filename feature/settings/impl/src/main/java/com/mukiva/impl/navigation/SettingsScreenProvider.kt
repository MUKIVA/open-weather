package com.mukiva.impl.navigation

import com.mukiva.api.navigation.ISettingsScreenProvider
import com.mukiva.core.navigation.IBaseScreen
import javax.inject.Inject

class SettingsScreenProvider @Inject constructor() : ISettingsScreenProvider {
    override val screen: IBaseScreen
        get() = SettingsScreen()
}
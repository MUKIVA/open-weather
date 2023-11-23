package com.mukiva.openweather.deps

import com.mukiva.api.ConfigStore
import com.mukiva.core.navigation.INavigator
import com.mukiva.impl.di.ISettingsDependencies
import com.mukiva.openweather.App

object SettingsDeps : ISettingsDependencies {
    override val navigator: INavigator
        get() = App.component.navigator
    override val configStore: ConfigStore
        get() = App.component.configStore

}
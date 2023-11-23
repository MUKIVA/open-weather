package com.mukiva.impl.di

import com.mukiva.api.ConfigStore
import com.mukiva.core.navigation.INavigator

interface ISettingsDependencies {
    val navigator: INavigator
    val configStore: ConfigStore
}
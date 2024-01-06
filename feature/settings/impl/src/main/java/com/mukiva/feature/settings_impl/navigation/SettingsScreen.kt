package com.mukiva.feature.settings_impl.navigation

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.mukiva.core.navigation.IBaseScreen
import com.mukiva.feature.settings_impl.R
import com.mukiva.feature.settings_impl.ui.SettingsTemplateFragment

class SettingsScreen(
    @StringRes val titleId: Int = R.string.fragment_settings_title
) : IBaseScreen {
    override val fragment: Fragment
        get() = SettingsTemplateFragment.newInstance(titleId)
}
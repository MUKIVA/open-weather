package com.mukiva.feature.settings.ui.adapter

import android.content.Context
import com.mukiva.feature.settings.R
import com.mukiva.feature.settings.domain.Group

object SettingStringResolver {
    fun Context.resolveName(group: Group): String {
        return when(group) {
            is Group.General.Theme -> getString(R.string.option_theme_name)
            is Group.General.UnitsType -> getString(R.string.option_units_type_name)
            is Group.General -> getString(R.string.setting_group_general)
        }
    }

    fun Context.resolveDescription(group: Group): String {
        return when(group) {
            is Group.General.Theme -> getString(R.string.option_theme_description)
            is Group.General.UnitsType -> getString(R.string.option_units_type_description)
            is Group.General -> ""
        }
    }

}
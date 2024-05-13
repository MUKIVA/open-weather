package com.mukiva.feature.settings.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.mukiva.open_weather.core.domain.settings.Group
import com.github.mukiva.open_weather.core.domain.settings.Theme
import com.github.mukiva.open_weather.core.domain.settings.UnitsType
import com.mukiva.feature.settings.R
import com.mukiva.feature.settings.domain.SettingItem
import kotlin.reflect.KClass

internal fun Context.resolveName(item: SettingItem): String {
    return when(item) {
        is SettingItem.Title -> resolveGroup(item.group)
        is SettingItem.Toggle -> resolveName(item.key)
        is SettingItem.Variant -> resolveName(item.key)
    }
}

internal fun Context.resolveDescription(item: SettingItem): String {
    return when(item) {
        is SettingItem.Title -> resolveGroup(item.group)
        is SettingItem.Toggle -> resolveDescription(item.key)
        is SettingItem.Variant -> resolveDescription(item.key)
    }
}

internal fun Context.resolveName(key: KClass<*>): String {
    return when(key) {
        Theme::class -> getString(R.string.option_theme_name)
        UnitsType::class -> getString(R.string.option_units_type_name)
        else -> error("Unimplemented brunch")
    }
}

internal fun Context.resolveDescription(key: KClass<*>): String {
    return when(key) {
        Theme::class -> getString(R.string.option_theme_description)
        UnitsType::class -> getString(R.string.option_units_type_description)
        else -> error("Unimplemented brunch")
    }
}

internal fun Context.resolveGroup (group: Group): String {
    return when(group) {
        is Group.General -> getString(R.string.setting_group_general)
    }
}

internal fun Fragment.asLocalTitle(type: KClass<*>): String {
    return when(type) {
        Theme::class -> getString(R.string.option_theme_name)
        UnitsType::class -> getString(R.string.option_units_type_name)
        else -> error("Not implemented")
    }
}

internal fun Fragment.asLocalVariants(type: KClass<*>): List<String> {
    return when(type) {
        Theme::class -> Theme.entries.map(::themeLocal)
        UnitsType::class -> UnitsType.entries.map(::unitsTypeLocal)
        else -> error("Not implemented")
    }
}

internal fun Context.asLocalVariant(type: Enum<*>): String {
    return when(type) {
        is Theme -> themeLocal(type)
        is UnitsType -> unitsTypeLocal(type)
        else -> error("Not implemented")
    }
}

internal fun Fragment.unitsTypeLocal(unitsType: UnitsType): String {
    return when(unitsType) {
        UnitsType.METRIC -> getString(R.string.option_units_type_metric)
        UnitsType.IMPERIAL -> getString(R.string.option_units_type_imperial)
    }
}

internal fun Context.unitsTypeLocal(unitsType: UnitsType): String {
    return when(unitsType) {
        UnitsType.METRIC -> getString(R.string.option_units_type_metric)
        UnitsType.IMPERIAL -> getString(R.string.option_units_type_imperial)
    }
}

internal fun Fragment.themeLocal(theme: Theme): String {
    return when(theme) {
        Theme.SYSTEM -> getString(R.string.option_theme_system)
        Theme.DARK -> getString(R.string.option_theme_dark)
        Theme.LIGHT -> getString(R.string.option_theme_light)
    }
}

internal fun Context.themeLocal(theme: Theme): String {
    return when(theme) {
        Theme.SYSTEM -> getString(R.string.option_theme_system)
        Theme.DARK -> getString(R.string.option_theme_dark)
        Theme.LIGHT -> getString(R.string.option_theme_light)
    }
}


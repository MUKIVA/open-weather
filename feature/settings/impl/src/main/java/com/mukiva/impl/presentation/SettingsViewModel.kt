package com.mukiva.impl.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.impl.domain.repository.SettingsRepository
import com.mukiva.api.Theme
import com.mukiva.api.UnitsType
import com.mukiva.api.domain.AppConfig
import com.mukiva.impl.domain.SettingItem
import com.mukiva.impl.domain.SettingVariant
import com.mukiva.impl.domain.config.Group
import com.mukiva.openweather.presentation.SingleStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    initialState: SettingsState,
    private val config: SettingsRepository
) : SingleStateViewModel<SettingsState, Nothing>(initialState) {

    private val mAppConfig = config.asAppConfig()

    init {
        mAppConfig
            .onEach { updateStruct(it) }
            .launchIn(viewModelScope)
    }

    fun onSelectOptionVariant(
        item: SettingItem.Variant,
        variant: SettingVariant
    ) = withViewModelScope {
        when (item.group) {
            is Group.General -> when(item.group) {
                Group.General.Theme -> {
                    config.setTheme(Theme.valueOf(variant.name))
                }
                Group.General.UnitsType -> {
                    config.setUnitsType(UnitsType.valueOf(variant.name))
                }
                else -> {}
            }
        }
    }

    private fun withViewModelScope(block: suspend() -> Unit) {
        viewModelScope.launch {
            block()
        }
    }

    fun onToggleOption() {
        // TODO(Impl toggle)
    }

    private fun updateStruct(cfg: AppConfig) = modifyState {
        copy(
            settingsList = buildList {
                add(SettingItem.Title(Group.General()))
                add(generateVariantItem(
                    group = Group.General.Theme,
                    enum = Theme::class.java,
                    configPropName = { cfg.theme.name }
                ))
                add(generateVariantItem(
                    group = Group.General.UnitsType,
                    enum = UnitsType::class.java,
                    configPropName = { cfg.units.name }
                ))
            }
        )
    }

    private fun generateVariantItem(
        group: Group,
        enum: Class<out Enum<*>>,
        configPropName: () -> String
    ): SettingItem.Variant {
        return SettingItem.Variant(
            groupType = group,
            variants = buildList {
                enum.enumConstants.forEach {
                    add(SettingVariant(
                        name = it.name,
                        isSelected = it.name == configPropName()
                    ))
                }
            }
        )
    }

}
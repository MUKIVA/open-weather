package com.mukiva.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.weather_data.SettingsRepository
import com.github.mukiva.open_weather.core.domain.settings.Config
import com.github.mukiva.open_weather.core.domain.settings.Group
import com.github.mukiva.open_weather.core.domain.settings.Theme
import com.github.mukiva.open_weather.core.domain.settings.UnitsType
import com.mukiva.feature.settings.domain.SettingItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.enums.EnumEntries
import kotlin.reflect.KClass

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val state: StateFlow<SettingsState>
        get() = mState.asStateFlow()

    private val mState = MutableStateFlow<SettingsState>(SettingsState.Init)

    fun loadConfiguration() {
        settingsRepository.getConfiguration()
            .map(::asState)
            .onEach(mState::emit)
            .launchIn(viewModelScope)
    }

    fun toggleOption() {

    }

    fun selectVariant(key: KClass<*>, variants: EnumEntries<*>, selectedVariant: Enum<*>) {
        val selectedPosition = variants.indexOf(selectedVariant)
        mState.update { state ->
            if (state is SettingsState.Content)
                state.copy(bottomSheetState = BottomSheetState.Show(key, variants, selectedPosition))
            else
                state
        }
    }

    fun commitSelection(position: Int) {
        val state = mState.value
        if (state !is SettingsState.Content) return
        if (state.bottomSheetState !is BottomSheetState.Show) return
        val variant = state.bottomSheetState.variants[position]
        viewModelScope.launch {
            when(state.bottomSheetState.key) {
                Theme::class ->
                    settingsRepository.setTheme(variant as Theme)
                UnitsType::class ->
                    settingsRepository.setUnitsType(variant as UnitsType)
            }
        }
    }

    fun closeBottomSheet() {
        mState.update { state ->
            if (state is SettingsState.Content)
                state.copy(bottomSheetState = BottomSheetState.Hide)
            else
                state
        }
    }

    private fun asState(config: Config): SettingsState {
        return SettingsState.Content(
            bottomSheetState = BottomSheetState.Hide,
            list = configurationAsState(config)
        )
    }

    private fun configurationAsState(config: Config) = buildList {
        config.groups.onEach { group ->
            when(group) {
                is Group.General -> {
                    add(SettingItem.Title(group))
                    add(SettingItem.Variant(
                        Theme::class,
                        group.theme,
                        Theme.entries
                    ))
                    add(SettingItem.Variant(
                        UnitsType::class,
                        group.unitsType,
                        UnitsType.entries
                    ))
                }
            }
        }
    }
}
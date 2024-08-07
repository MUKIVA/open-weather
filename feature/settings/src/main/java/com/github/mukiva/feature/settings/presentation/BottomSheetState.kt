package com.github.mukiva.feature.settings.presentation

import kotlin.enums.EnumEntries
import kotlin.reflect.KClass

internal sealed class BottomSheetState {
    data object Hide : BottomSheetState()
    data class Show(
        val key: KClass<*>,
        val variants: EnumEntries<*>,
        val selectedPosition: Int,
    ) : BottomSheetState()
}

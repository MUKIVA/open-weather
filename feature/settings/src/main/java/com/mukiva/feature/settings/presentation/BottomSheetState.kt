package com.mukiva.feature.settings.presentation

import kotlin.enums.EnumEntries
import kotlin.reflect.KClass

sealed class BottomSheetState {
    data object Hide : BottomSheetState()
    data class Show(
        val key: KClass<*>,
        val variants: EnumEntries<*>,
        val selectedPosition: Int,
    ) : BottomSheetState()
}
package com.github.mukiva.openweather.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

internal abstract class AbstractRootComponent(
    componentContext: ComponentContext
) : CommonRootComponent, ComponentContext by componentContext {

    @Serializable
    sealed interface Config {
        data object Onboarding : Config
        data object Main : Config
        data object Settings : Config
        data object LocationManager : Config
    }

    abstract val slot: Value<ChildSlot<*, CommonRootComponent.Child>>

    protected val navigation = SlotNavigation<Config>()

}
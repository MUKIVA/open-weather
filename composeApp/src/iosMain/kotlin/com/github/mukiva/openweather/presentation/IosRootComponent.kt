package com.github.mukiva.openweather.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value

internal class IosRootComponent(
    componentContext: ComponentContext
) : AbstractRootComponent(componentContext) {

    override val slot: Value<ChildSlot<*, CommonRootComponent.Child>> =
        childSlot(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = { Config.Main },
            handleBackButton = true,
            childFactory = TODO()
        )

    override val platform: CommonRootComponent.Platform
        get() = CommonRootComponent.Platform.IOS

}
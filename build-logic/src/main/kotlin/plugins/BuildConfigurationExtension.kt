package plugins

import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

abstract class BuildConfigurationExtension {
    abstract val minSdk: Property<Int>
    abstract val targetSdk: Property<Int>
    abstract val compileSdk: Property<Int>
}

fun BuildConfigurationExtension.minSdk(version: Provider<String>) {
    val intVersion = version.get().toIntOrNull()
        ?: error("Couldn't get the minSdk version")
    minSdk.set(intVersion)
}

fun BuildConfigurationExtension.targetSdk(version: Provider<String>) {
    val intVersion = version.get().toIntOrNull()
        ?: error("Couldn't get the targetSdk version")
    targetSdk.set(intVersion)
}

fun BuildConfigurationExtension.compileSdk(version: Provider<String>) {
    val intVersion = version.get().toIntOrNull()
        ?: error("Couldn't get the compileSdk version")
    compileSdk.set(intVersion)
}


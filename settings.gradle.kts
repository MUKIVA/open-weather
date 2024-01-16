pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "open-weather"

include(":app")
include(":core:presentation")
include(":core:usecase")
include(":core:ui")
include(":core:navigation")
include(":core:network")
include(":core:data")

include(":feature:settings")
include(":feature:dashboard")
include(":feature:location_manager")
include(":feature:forecast")

include(":navigation")

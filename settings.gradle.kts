pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "Open Weather"

include(":app")
include(":core:presentation")
include(":core:usecase")
include(":core:ui")
include(":core:navigation")

include(":feature:settings:impl")
include(":feature:settings:api")

include(":feature:dashboard:api")
include(":feature:dashboard:impl")

include(":feature:location_manager:api")
include(":feature:location_manager:impl")
include(":core:network")

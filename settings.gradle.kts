pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "Open Weather"
include(":app")
include(":core:data")
include(":core:domain")
include(":feature:current_weather")
include(":core:di")
include(":core:presentation")
include(":core:usecase")
include(":feature:location_search")
include(":core:ui")
include(":feature:location_store")

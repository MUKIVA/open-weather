@file:Suppress("UnstableApiUsage")

include(":weather-settings-data")


include(":core:domain")


pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "open-weather"

include(":app")
include(":core:ui")
include(":core:data")
include(":core:presentation")

include(":feature:settings")
include(":feature:dashboard")
include(":feature:location_manager")
include(":feature:forecast")

include(":navigation")

include(":weather-data")
include(":database")
include(":weather-api")

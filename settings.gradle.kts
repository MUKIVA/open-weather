@file:Suppress("UnstableApiUsage")

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
include(":core:domain")

include(":feature:settings")
include(":feature:dashboard")
include(":feature:location-manager")
include(":feature:forecast")
include(":feature:weather-notification")
include(":feature:splash")

include(":navigation")

include(":weather-data")
include(":weather-database")
include(":weather-api")

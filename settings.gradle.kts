@file:Suppress("UnstableApiUsage")

include(":weather-api")


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

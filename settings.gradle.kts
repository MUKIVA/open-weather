rootProject.name = "open-weather"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")

include(":core:theme")
include(":core:data")

include(":feature:main:api")
include(":feature:main:impl")

include(":feature:onboarding:api")
include(":feature:onboarding:impl")

include(":feature:settings:api")
include(":feature:settings:impl")

include(":feature:manager:api")
include(":feature:manager:impl")
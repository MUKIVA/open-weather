plugins {
    GradlePlugins.run {
        id(androidApplication.id)
        id(sdk.id)
        id(kotlinAndroid.id)
        id(defaultFeature.id)
        id(secrets.id)
        id(hilt.id)
        id(ksp.id)
    }
}

android {
    namespace = "com.mukiva.openweather"

    defaultConfig {
        applicationId = "com.mukiva.openweather"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName(BuildType.DEBUG) {
            applicationIdSuffix = ".debug"
            isShrinkResources = false

            signingConfig = signingConfigs.findByName("debug")
        }
        getByName(BuildType.RELEASE) {
            applicationIdSuffix = ""
            isShrinkResources = true

            signingConfig = signingConfigs.findByName("release")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    coreScope(
        Projects.Core.navigation,
        Projects.Core.ui,
        Projects.Core.network
    )

    featureScope(
        Projects.Feature.dashboard,
        Projects.Feature.forecast,
        Projects.Feature.locationManager,
        Projects.Feature.settings
    )

    implementation(project(":navigation"))

    implementation(Deps.AndroidX.Navigation.FRAGMENT)
    implementation(Deps.AndroidX.Navigation.UI)
    implementation(Deps.OkHttp.LOGGING_INTERCEPTOR)

    addDefaultImpl()
    addHilt()
    addRetrofit()
}

secrets {
    // Change the properties file from the default "local.properties" in your root project
    // to another properties file in your root project.
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be checked in version
    // control.
    defaultPropertiesFileName = "secrets.defaults.properties"

}
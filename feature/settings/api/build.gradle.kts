plugins {
    GradlePlugins.run {
        id(androidLib.id)
        id(sdk.id)
        id(kotlinAndroid.id)
        id(defaultFeature.id)
    }
}

android {
    namespace = "com.mukiva.feature.settings_api"
}

dependencies {
    coreScope(Projects.Core.navigation)
    implementation(Deps.AndroidX.CORE_KTX)
}
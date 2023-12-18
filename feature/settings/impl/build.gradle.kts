plugins {
    GradlePlugins.run {
        id(androidLib.id)
        id(sdk.id)
        id(kotlinAndroid.id)
        id(defaultFeature.id)
        id(hilt.id)
        id(ksp.id)
    }
}

android {
    namespace = "com.mukiva.feature.settings_impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    coreScope(
        Projects.Core.ui,
        Projects.Core.navigation,
        Projects.Core.presentation,
        Projects.Core.network
    )

    featureScope(
        Projects.Feature.settings,
        addMethod = AddFeatureMethod.ONLY_API
    )

    addDefaultImpl()
    addHilt()
    addDataStore()
}
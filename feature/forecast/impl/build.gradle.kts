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
    namespace = "com.mukiva.feature.forecast_impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    coreScope(
        Projects.Core.presentation,
        Projects.Core.usecase,
        Projects.Core.ui,
        Projects.Core.navigation,
        Projects.Core.network,
        Projects.Core.data
    )

    featureScope(
        Projects.Feature.forecast,
        addMethod = AddFeatureMethod.ONLY_API
    )

    addDefaultImpl()
    addHilt()
    addRetrofit()

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

}
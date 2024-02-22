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
    namespace = "com.mukiva.feature.forecast"

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
        Projects.Core.network
    )

    addDefaultImpl()
    addHilt()
    addRetrofit()

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

}
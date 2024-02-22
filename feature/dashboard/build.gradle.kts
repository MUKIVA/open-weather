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
    namespace = "com.mukiva.feature.dashboard"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    coreScope(
        Projects.Core.presentation,
        Projects.Core.navigation,
        Projects.Core.usecase,
        Projects.Core.ui,
        Projects.Core.network
    )

    addDefaultImpl()
    addHilt()
    addRetrofit()
    addRoom()

    implementation("com.github.bumptech.glide:glide:4.16.0")
}
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
    namespace = "com.mukiva.navigation"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    coreScope(
        Projects.Core.ui
    )

    implementation(Deps.AndroidX.Navigation.FRAGMENT)
    implementation(Deps.AndroidX.Navigation.UI)

    addDefaultImpl()
    addHilt()
}
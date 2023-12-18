plugins {
    GradlePlugins.run {
        id(androidLib.id)
        id(sdk.id)
        id(kotlinAndroid.id)
        id(defaultFeature.id)
    }
}

android {
    namespace = "com.mukiva.core.network"
}

dependencies {
    implementation(Deps.AndroidX.CORE_KTX)
}
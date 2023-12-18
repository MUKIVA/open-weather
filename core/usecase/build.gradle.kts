plugins {
    GradlePlugins.run {
        id(androidLib.id)
        id(sdk.id)
        id(kotlinAndroid.id)
        id(defaultFeature.id)
    }
}

android {
    namespace = "com.mukiva.core.usecase"
}

dependencies {
    implementation(Deps.AndroidX.CORE_KTX)
    implementation(Deps.Kotlin.COROUTINES_CORE)
    implementation(Deps.Kotlin.COROUTINES_ANDROID)
}
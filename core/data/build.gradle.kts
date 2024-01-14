plugins {
    GradlePlugins.run {
        id(androidLib.id)
        id(sdk.id)
        id(kotlinAndroid.id)
        id(defaultFeature.id)
    }
}

android {
    namespace = "com.mukiva.core.data"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    addDefaultImpl()

    implementation("com.google.code.gson:gson:2.10.1")

//    implementation(Deps.AndroidX.CORE_KTX)
//    implementation(Deps.Google.MATERIAL)
}
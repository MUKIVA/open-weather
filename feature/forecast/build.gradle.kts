plugins {
    alias(libs.plugins.androidLib)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.androidDefault)
}

android {
    namespace = "com.github.mukiva.feature.forecast"
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.ui)

    implementation(projects.weatherData)

    implementation(libs.androidx.core)
    implementation(libs.android.material)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.fragment)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.retrofit)

    implementation(libs.androidx.swipe)

    implementation(libs.kotlinx.datetime)
}

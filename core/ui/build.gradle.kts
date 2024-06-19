plugins {
    alias(libs.plugins.androidLib)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.androidDefault)
}

android {
    namespace = "com.github.mukiva.core.ui"
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.androidx.core)
    implementation(libs.android.material)
}

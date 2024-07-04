plugins {
    alias(libs.plugins.androidLib)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidDefault)
}

android {
    namespace = "com.github.mukiva.weatherdata"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.weatherDatabase)
    implementation(projects.weatherApi)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.datastore)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.secrets)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.androidDefault)
}

android {
    namespace = "com.github.mukiva.openweather"

    defaultConfig {
        applicationId = "com.github.mukiva.openweather"
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.domain)
    implementation(projects.navigation)
    implementation(projects.feature.dashboard)
    implementation(projects.feature.forecast)
    implementation(projects.feature.locationManager)
    implementation(projects.feature.settings)
    implementation(projects.feature.splash)
    implementation(projects.feature.weatherNotification)
    implementation(projects.weatherData)
    implementation(projects.weatherApi)
    implementation(projects.weatherDatabase)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.okhttp.logging)

    implementation(libs.androidx.core)
    implementation(libs.android.material)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.fragment)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.json)
    implementation(libs.kotlinx.datetime)

    implementation(libs.kotlinx.serialization.json)
}

secrets {
    // Change the properties file from the default "local.properties" in your root project
    // to another properties file in your root project.
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be checked in version
    // control.
    defaultPropertiesFileName = "secrets.defaults.properties"
}

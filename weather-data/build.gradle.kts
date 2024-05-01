plugins {
    alias(libs.plugins.androidLib)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.github.mukiva.weather_data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(project(":weather-api"))
    implementation(project(":database"))

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.android)
}
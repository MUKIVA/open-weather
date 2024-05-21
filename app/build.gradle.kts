plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.secrets)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinxSerialization)
}

android {
    namespace = "com.github.mukiva.openweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.mukiva.openweather"
        versionCode = 1
        versionName = "1.0"
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isShrinkResources = false

            signingConfig = signingConfigs.findByName("debug")
        }
        getByName("release") {
            applicationIdSuffix = ".release"
            isShrinkResources = false

            signingConfig = signingConfigs.findByName("release")
        }
        create("profile") {
            applicationIdSuffix = ".profile"
            isProfileable = true
            isDebuggable = false

            signingConfig = signingConfigs.findByName("debug")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))

    implementation(project(":navigation"))

    implementation(project(":feature:dashboard"))
    implementation(project(":feature:forecast"))
    implementation(project(":feature:location-manager"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:splash"))

    implementation(project(":weather-data"))
    implementation(project(":weather-api"))
    implementation(project(":weather-database"))

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

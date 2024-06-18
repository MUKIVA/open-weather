plugins {
    alias(libs.plugins.androidLib)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.github.mukiva.feature.weathernotification"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }

    buildTypes {
        create("profile") {
            signingConfig = signingConfigs.findByName("debug")
        }
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
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.weatherData)

    implementation(libs.androidx.core)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}

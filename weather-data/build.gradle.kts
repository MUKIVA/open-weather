plugins {
    alias(libs.plugins.androidLib)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.github.mukiva.weatherdata"
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

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.datastore)
}

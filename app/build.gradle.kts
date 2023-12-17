import com.android.build.api.variant.BuildConfigField
import com.mukiva.buildsrc.Projects
import com.mukiva.buildsrc.Versions
import com.mukiva.buildsrc.addHilt
import com.mukiva.buildsrc.addRetrofit
import com.mukiva.buildsrc.coreScope
import com.mukiva.buildsrc.featureScope

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.mukiva.openweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mukiva.openweather"
        minSdk = 24
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }

    }

    buildTypes {
        debug {
            BuildConfigField("String", "API_KEY", "1233a469bde749668bd95921230310")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            BuildConfigField("String", "API_KEY", "1233a469bde749668bd95921230310")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}

dependencies {

    coreScope(
        Projects.Core.navigation,
        Projects.Core.ui,
        Projects.Core.network
    )

    featureScope(
        Projects.Feature.dashboard,
        Projects.Feature.locationManager,
        Projects.Feature.settings
    )

    addHilt()
    addRetrofit()

    implementation("androidx.core:core-ktx:${Versions.KOTLIN}")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")

    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}")
}

secrets {
    // Change the properties file from the default "local.properties" in your root project
    // to another properties file in your root project.
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be checked in version
    // control.
    defaultPropertiesFileName = "secrets.defaults.properties"

}
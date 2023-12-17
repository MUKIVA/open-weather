import com.mukiva.buildsrc.AddFeatureMethod
import com.mukiva.buildsrc.Projects
import com.mukiva.buildsrc.addDefaultImpl
import com.mukiva.buildsrc.addHilt
import com.mukiva.buildsrc.addRetrofit
import com.mukiva.buildsrc.addRoom
import com.mukiva.buildsrc.coreScope
import com.mukiva.buildsrc.featureScope

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.mukiva.feature.location_manager_impl"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        Projects.Core.presentation,
        Projects.Core.usecase,
        Projects.Core.ui,
        Projects.Core.navigation,
        Projects.Core.network,
    )

    featureScope(
        Projects.Feature.locationManager,
        addMethod = AddFeatureMethod.ONLY_API
    )

    addHilt()
    addRetrofit()
    addRoom()
    addDefaultImpl()
}
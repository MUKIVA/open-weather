import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
//        androidMain.dependencies {
//            implementation(compose.preview)
//            implementation(libs.androidx.activity.compose)
//        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
//            implementation(compose.ui)
//            implementation(compose.components.resources)
//            implementation(compose.components.uiToolingPreview)
//            implementation(libs.androidx.lifecycle.viewmodelCompose)
//            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
//        commonTest.dependencies {
//            implementation(libs.kotlin.test)
//        }
    }

}

android {
    namespace = "com.github.mukiva.openweather.core.theme"
    compileSdk =libs.versions.android.compileSdk.get().toInt()
}
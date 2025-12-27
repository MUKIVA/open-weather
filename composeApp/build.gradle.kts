import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.koltinxSerialization)
}

kotlin {
    androidLibrary {
        namespace = "com.github.mukiva.openweather"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava()
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
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.decompose.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation(libs.decompose)
            implementation(libs.decompose.compose)

            implementation(libs.kotlinx.serialization.json)

            implementation(projects.core.theme)
            implementation(projects.core.data)

            implementation(projects.feature.main.api)
            implementation(projects.feature.main.impl)

            implementation(projects.feature.manager.api)
            implementation(projects.feature.manager.impl)

            implementation(projects.feature.onboarding.api)
            implementation(projects.feature.onboarding.impl)

            implementation(projects.feature.settings.api)
            implementation(projects.feature.settings.impl)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

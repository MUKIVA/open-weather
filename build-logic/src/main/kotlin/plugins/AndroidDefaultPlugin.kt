@file:Suppress("Deprecation")

package plugins

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

class AndroidDefaultPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.configureDefault()
    }

    @Suppress("DEPRECATION")
    private fun Project.configureDefault() = configure<BaseExtension> {
        val instance = this

        defaultConfig {
            val config = rootProject.extensions.getByType<BuildConfigurationExtension>()
            minSdk = config.minSdk.get()
            targetSdk = config.targetSdk.get()
            compileSdkVersion(config.compileSdk.get())

            val proguardFiles = rootProject.fileTree("proguard").files +
                getDefaultProguardFile("proguard-android-optimize.txt")

            proguardFiles(*(proguardFiles.toTypedArray()))
        }

        buildTypes {
            getByName("debug") {
                if (instance !is LibraryExtension) {
                    applicationIdSuffix = ".debug"
                }
                isMinifyEnabled = false
                isDebuggable = true

                signingConfig = signingConfigs.findByName("debug")
            }
            getByName("release") {
                isMinifyEnabled = true
                isDebuggable = false

                signingConfig = signingConfigs.findByName("release")
            }
            create("profile") {
                initWith(getByName("debug"))
                if (instance !is LibraryExtension) {
                    applicationIdSuffix = ".profile"
                }
                isDebuggable = false
                isProfileable = true
                isMinifyEnabled = true

                signingConfig = signingConfigs.findByName("debug")
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        (this as ExtensionAware).configure<KotlinJvmOptions> {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }

        sourceSets.forEach { source ->
            source.java.srcDirs("src/${source.name}/kotlin")
        }
    }
}

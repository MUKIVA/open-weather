import BuildType
import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

class DefaultProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.configureDefault()
    }

    private fun Project.configureDefault() = configure<BaseExtension> {
        defaultConfig {
            val proguardFiles = rootProject.fileTree("proguard").files +
                    getDefaultProguardFile("proguard-android-optimize.txt")
            proguardFiles(*proguardFiles.toTypedArray())
        }

        buildTypes {
            getByName(BuildType.DEBUG) {
                isMinifyEnabled = false
                isDebuggable = true
            }

            getByName(BuildType.RELEASE) {
                isMinifyEnabled = true
                isDebuggable = false
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }


        sourceSets.forEach { it.java.srcDir("src/${it.name}/kotlin") }
    }

}
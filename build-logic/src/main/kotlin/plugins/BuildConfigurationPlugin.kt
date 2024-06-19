package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

private const val EXTENSION_NAME = "buildConfiguration"

class BuildConfigurationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.extensions.create<BuildConfigurationExtension>(EXTENSION_NAME)
    }
}

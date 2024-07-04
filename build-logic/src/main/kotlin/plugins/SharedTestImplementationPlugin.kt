package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

private const val SHARED_TEST_IMPLEMENTATION_NAME = "sharedTestImplementation"

class SharedTestImplementationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.extensions.create<SharedTestExtension>(SHARED_TEST_IMPLEMENTATION_NAME)
    }

}
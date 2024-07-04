package plugins

enum class ConfName {
    TEST_IMPLEMENTATION,
    ANDROID_TEST_IMPLEMENTATION
}

data class Dependency(
    val confName: ConfName,
    val dependencyNotation: Any
)

open class SharedTestExtension {
    val dependencies: List<Dependency>
        get() = mDependencies.toList()

    private val mDependencies = mutableListOf<Dependency>()

    fun addDependency(dependency: Dependency) {
        mDependencies.add(dependency)
    }
}

fun SharedTestExtension.testImplementation(dependencyNotation: Any) {
    addDependency(
        Dependency(
            confName = ConfName.TEST_IMPLEMENTATION,
            dependencyNotation = dependencyNotation
        )
    )
}

fun SharedTestExtension.androidTestImplementation(dependencyNotation: Any) {
    addDependency(
        Dependency(
            confName = ConfName.ANDROID_TEST_IMPLEMENTATION,
            dependencyNotation = dependencyNotation
        )
    )
}

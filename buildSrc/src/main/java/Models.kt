
object BuildType {
    const val RELEASE = "release"
    const val DEBUG = "debug"
}

data class FeatureModule(
    val name: String
) {
    companion object {
        fun create(moduleName: String) = FeatureModule(
            name = ":feature:$moduleName",
        )
    }
}

data class CoreModule(
    val path: String
) {
    companion object {
        fun create(moduleName: String) = CoreModule(
            path = ":core:$moduleName"
        )
    }
}

data class Plugin(
    val id: String,
    val version: String
)
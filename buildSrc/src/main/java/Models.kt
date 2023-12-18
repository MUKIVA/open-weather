
object BuildType {
    const val RELEASE = "release"
    const val DEBUG = "debug"
}

enum class AddFeatureMethod {
    ALL,
    ONLY_IMPL,
    ONLY_API
}

data class FeatureModule(
    val api: String,
    val impl: String
) {
    companion object {
        fun create(moduleName: String) = FeatureModule(
            api = ":feature:$moduleName:api",
            impl = ":feature:$moduleName:impl"
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
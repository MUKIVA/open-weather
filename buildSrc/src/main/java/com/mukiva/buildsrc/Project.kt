package com.mukiva.buildsrc

data class CoreModule(
    val path: String
) {
    companion object {
        fun create(moduleName: String) = CoreModule(
            path = ":core:$moduleName"
        )
    }
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

object Projects {
    object Core {
        val navigation = CoreModule.create("navigation")
        val network = CoreModule.create("network")
        val presentation = CoreModule.create("presentation")
        val ui = CoreModule.create("ui")
        val usecase = CoreModule.create("usecase")
    }

    object Feature {
        val dashboard = FeatureModule.create("dashboard")
        val locationManager = FeatureModule.create("location_manager")
        val settings = FeatureModule.create("settings")
    }
}
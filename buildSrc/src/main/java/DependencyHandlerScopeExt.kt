import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.addDefaultImpl() {
    add("implementation", Deps.AndroidX.CORE_KTX)
    add("implementation", Deps.Google.MATERIAL)
    add("implementation", Deps.AndroidX.VM_LIFECYCLE)
    add("implementation", Deps.AndroidX.FRAGMENT)
}

fun DependencyHandlerScope.addRoom() {
    add("implementation", Deps.AndroidX.Room.RUNTIME)
    add("implementation", Deps.AndroidX.Room.KTX)
    add("ksp", Deps.AndroidX.Room.COMPILER)
}

fun DependencyHandlerScope.addRetrofit() {
    add("implementation", Deps.Retrofit.RETROFIT)
    add("implementation", Deps.Retrofit.CONVERTER_GSON)
}

fun DependencyHandlerScope.addHilt() {
    add("implementation", Deps.Google.Hilt.HILT_ANDROID)
    add("ksp", Deps.Google.Hilt.HILT_COMPILER)
}

fun DependencyHandlerScope.addDataStore() {
    add("implementation", Deps.AndroidX.DataStore.DATA_STORE)
}

fun DependencyHandlerScope.coreScope(vararg items: CoreModule) {
    items.forEach { item ->
        add("api", project(item.path))
    }
}

fun DependencyHandlerScope.featureScope(
    vararg items: FeatureModule,
    addMethod: AddFeatureMethod = AddFeatureMethod.ALL
) {
    items.forEach { item ->
        if (addMethod == AddFeatureMethod.ALL || addMethod == AddFeatureMethod.ONLY_API)
            add("implementation", project(item.api))
        if (addMethod == AddFeatureMethod.ALL || addMethod == AddFeatureMethod.ONLY_IMPL)
            add("implementation", project(item.impl))
    }
}
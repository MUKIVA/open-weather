package com.mukiva.buildsrc

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.addDefaultImpl() {
    add("implementation","androidx.core:core-ktx:${Versions.KOTLIN}")
    add("implementation","com.google.android.material:material:1.10.0")
    add("implementation","androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    add("implementation","androidx.fragment:fragment-ktx:1.6.2")
}

fun DependencyHandlerScope.addRoom() {
    add("implementation", Deps.Room.runtime)
    add("ksp", Deps.Room.compiler)
    add("implementation", Deps.Room.ktx)
}

fun DependencyHandlerScope.addRetrofit() {
    add("implementation", Deps.Retrofit.retrofit)
    add("implementation", Deps.Retrofit.converterGson)
}

fun DependencyHandlerScope.addHilt() {
    add("implementation", Deps.Hilt.hiltAndroid)
    add("kapt", Deps.Hilt.hiltCompiler)
}

fun DependencyHandlerScope.addDataStore() {
    add("implementation", Deps.DataStore.dataStore)
}

fun DependencyHandlerScope.coreScope(vararg items: CoreModule) {
    items.forEach { item ->
        add("implementation", project(item.path))
    }
}

enum class AddFeatureMethod {
    ALL,
    ONLY_IMPL,
    ONLY_API
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
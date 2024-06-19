import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import plugins.compileSdk
import plugins.minSdk
import plugins.targetSdk

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
    alias(libs.plugins.androidLib) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.kotlinParcelize) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.androidBuildConfiguration)
    alias(libs.plugins.androidDefault) apply false
}

buildConfiguration {
    minSdk(libs.versions.minSdk)
    targetSdk(libs.versions.targetSdk)
    compileSdk(libs.versions.compileSdk)
}

buildscript {
    dependencies {
        classpath(libs.build.logic)
    }
}

detekt {
    toolVersion = "1.23.3"
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true

    source.setFrom("src/main/java", "src/main/kotlin")
    debug = true
    basePath = projectDir.absolutePath
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = JavaVersion.VERSION_17.toString()
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = JavaVersion.VERSION_17.toString()
}

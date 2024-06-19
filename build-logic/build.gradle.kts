plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

group = "com.github.mukiva"
version = "1.0"

gradlePlugin {
    plugins {
        create("android-default") {
            id = "$group.android-default"
            implementationClass = "plugins.AndroidDefaultPlugin"
        }
        create("build-configuration") {
            id = "$group.build-configuration"
            implementationClass = "plugins.BuildConfigurationPlugin"
        }
    }
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
}

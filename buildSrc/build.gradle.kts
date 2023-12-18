plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("root-project") {
            id = "ru.mukiva.root-project"
            implementationClass = "RootProjectPlugin"
        }
        create("sdk-version") {
            id = "ru.mukiva.sdk-version"
            implementationClass = "SdkVersionPlugin"
        }
        create("default-project") {
            id = "ru.mukiva.default-project"
            implementationClass = "DefaultProjectPlugin"
        }
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:8.2.0")
    implementation(kotlin("gradle-plugin", version = "1.9.0"))
    implementation("com.squareup:javapoet:1.13.0")
}
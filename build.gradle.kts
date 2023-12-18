// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    with(GradlePlugins) {
        id(androidApplication.id) apply false
        id(kotlinAndroid.id) apply false

        id(secrets.id) version secrets.version apply false
        id(hilt.id) version hilt.version apply false
        id(ksp.id) version ksp.version apply false

        id(root.id)
    }

}

buildConfiguration {
    minSdk.set(SdkCompileOption.MIN_SDK)
    targetSdk.set(SdkCompileOption.TARGET_SDK)
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
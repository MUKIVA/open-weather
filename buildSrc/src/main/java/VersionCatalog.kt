object SdkCompileOption {
    const val MIN_SDK = 24
    const val TARGET_SDK = 34
}

object Versions {
    const val HILT = "2.49"
    const val RETROFIT = "2.9.0"
    const val KOTLIN = "1.12.0"
    const val NAVIGATION = "2.7.6"
    const val ROOM = "2.6.1"
    const val DATA_STORE = "1.0.0"
    const val KSP = "1.9.21-1.0.16"
    const val SECRETS = "2.0.1"
    const val FRAGMENT = "1.6.2"
    const val VM_LIFECYCLE = "2.6.2"
    const val MATERIAL = "1.10.0"
    const val COROUTINES = "1.7.3"
    const val OK_HTTP = "3.4.1"

    const val ANDROID_GRADLE = "8.2.0"
}

object Deps {
    object AndroidX {
        const val CORE_KTX = "androidx.core:core-ktx:${Versions.KOTLIN}"
        const val FRAGMENT = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT}"
        const val VM_LIFECYCLE = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.VM_LIFECYCLE}"

        object Room {
            const val RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
            const val COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
            const val KTX = "androidx.room:room-ktx:${Versions.ROOM}"
        }

        object DataStore {
            const val DATA_STORE = "androidx.datastore:datastore-preferences:${Versions.DATA_STORE}"
        }

        object Navigation {
            const val FRAGMENT = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
            const val UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
        }

    }

    object Kotlin {
        const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
        const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
    }

    object Google {

        const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"

        object Hilt {
            const val HILT_ANDROID = "com.google.dagger:hilt-android:${Versions.HILT}"
            const val HILT_COMPILER = "com.google.dagger:hilt-compiler:${Versions.HILT}"
        }

    }

    object Retrofit {
        const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
        const val CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    }

    object OkHttp {
        const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Versions.OK_HTTP}"
    }
}

object GradlePlugins {
    val androidApplication = Plugin(
        id = "com.android.application",
        version = Versions.ANDROID_GRADLE
    )
    val androidLib = Plugin(
        id = "com.android.library",
        version = Versions.ANDROID_GRADLE
    )
    val hilt = Plugin(
        id = "com.google.dagger.hilt.android",
        version = Versions.HILT
    )
    val kotlinAndroid = Plugin(
        id = "org.jetbrains.kotlin.android",
        version = Versions.KOTLIN
    )
    val ksp = Plugin(
        id = "com.google.devtools.ksp",
        version = Versions.KSP
    )
    val secrets = Plugin(
        id = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin",
        version = Versions.SECRETS
    )

    val root = Plugin(
        id = "ru.mukiva.root-project",
        version = "NONE"
    )

    val sdk = Plugin(
        id = "ru.mukiva.sdk-version",
        version = "NONE"
    )

    val defaultFeature = Plugin(
        id = "ru.mukiva.default-project",
        version = "NONE"
    )
}

object Projects {
    object Core {
        val navigation = CoreModule.create("navigation")
        val network = CoreModule.create("network")
        val presentation = CoreModule.create("presentation")
        val ui = CoreModule.create("ui")
        val usecase = CoreModule.create("usecase")
        val data = CoreModule.create("data")
    }

    object Feature {
        val dashboard = FeatureModule.create("dashboard")
        val locationManager = FeatureModule.create("location_manager")
        val settings = FeatureModule.create("settings")
        val forecast = FeatureModule.create("forecast")
    }
}
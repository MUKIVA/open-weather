package com.mukiva.buildsrc

object Deps {

    object Room {
        const val runtime = "androidx.room:room-runtime:${Versions.ROOM}"
        const val compiler = "androidx.room:room-compiler:${Versions.ROOM}"
        const val ktx = "androidx.room:room-ktx:${Versions.ROOM}"
    }

    object Hilt {
        const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.HILT}"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.HILT}"
    }

    object Retrofit {
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
        const val converterGson = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    }

    object DataStore {
        const val dataStore = "androidx.datastore:datastore-preferences:${Versions.DATA_STORE}"
    }
}
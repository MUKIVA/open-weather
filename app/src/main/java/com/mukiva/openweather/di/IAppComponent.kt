package com.mukiva.openweather.di

import android.app.Application
import com.mukiva.api.ConfigStore
import com.mukiva.core.navigation.INavigator
import com.mukiva.openweather.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@[AppScope Singleton Component(modules = [AppModule::class])]
interface IAppComponent {

    val config: AppConfig
    val application: Application
    val navigator: INavigator
    val configStore: ConfigStore

    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        @BindsInstance
        fun config(cfg: AppConfig): Builder
        fun build(): IAppComponent
    }

}

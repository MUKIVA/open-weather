package com.mukiva.openweather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.mukiva.core.navigation.INavigator
import com.mukiva.feature.dashboard_impl.navigation.DashboardScreen
import com.mukiva.openweather.R
import com.mukiva.feature.settings_impl.domain.repository.SettingsRepository
import com.mukiva.feature.settings_api.Theme
import com.mukiva.openweather.navigator.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var navigator: INavigator
    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val mAppConfig by lazy {
        settingsRepository.asAppConfig()
            .onEach { updateAppTheme(it.theme) }
            .launchIn(lifecycleScope)
    }

    private val mFragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
            }
        }

    }

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        mAppConfig

        if (savedInstanceState == null) {
            (navigator as MainNavigator).launchFragment(
                this,
                DashboardScreen(),
                addToBackStack = false
            )
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            mFragmentCallbacks,
            false
        )
    }

    private fun updateAppTheme(theme: Theme) {
        when(theme) {
            Theme.SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            Theme.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            Theme.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        (navigator as MainNavigator)
            .whenMainActivityActive
            .mainActivity = this
    }

    override fun onPause() {
        super.onPause()
        (navigator as MainNavigator)
            .whenMainActivityActive.mainActivity = null
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(mFragmentCallbacks)
    }

}

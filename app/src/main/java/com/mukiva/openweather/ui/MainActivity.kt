package com.mukiva.openweather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mukiva.core.navigation.INavigator
import com.mukiva.current_weather.navigation.CurrentWeatherScreen
import com.mukiva.openweather.App
import com.mukiva.openweather.R
import com.mukiva.openweather.navigator.MainNavigator
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var navigator: INavigator

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

        val app = application as App
        app.appComponent.inject(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (savedInstanceState == null) {
            (navigator as MainNavigator).launchFragment(
                this,
                CurrentWeatherScreen(),
                addToBackStack = false
            )
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            mFragmentCallbacks,
            false
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
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

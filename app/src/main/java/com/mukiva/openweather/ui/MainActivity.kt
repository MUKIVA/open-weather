package com.mukiva.openweather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.mukiva.current_weather.ui.CurrentWeatherFragment
import com.mukiva.openweather.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainContainer, CurrentWeatherFragment.newInstance())
                .commit()
        }

    }

}

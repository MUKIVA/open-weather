package com.mukiva.openweather.navigator

import android.app.Application
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import com.mukiva.core.navigation.IBaseScreen
import com.mukiva.core.navigation.INavigator
import com.mukiva.openweather.R
import com.mukiva.openweather.ui.MainActivity

class MainNavigator(
    application: Application
) : AndroidViewModel(application), INavigator {

    val whenMainActivityActive = MainActivityActions()
    override fun launch(screen: IBaseScreen) = whenMainActivityActive { activity ->
        launchFragment(activity, screen)
    }

    override fun goBack() = whenMainActivityActive {
        it.onBackPressed()
    }

    override fun getString(msgRes: Int): String {
        return getApplication<Application>().getString(msgRes)
    }

    override fun onCleared() {
        super.onCleared()
        whenMainActivityActive.clear()
    }

    fun launchFragment(
        activity: MainActivity,
        screen: IBaseScreen,
        addToBackStack: Boolean = true
    ) {
        val fragment = screen.fragment
        fragment.arguments = bundleOf(ARGS_SCREEN_KEY to screen)
        val transaction = activity.supportFragmentManager.beginTransaction()
        if (addToBackStack)
            transaction.addToBackStack(null)
        transaction.replace(R.id.mainContainer, fragment)
        transaction.commit()
    }

    companion object {
        const val ARGS_SCREEN_KEY = "ARGS_SCREEN_KEY"
    }
}
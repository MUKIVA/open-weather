package com.mukiva.openweather.navigator

import android.app.Application
import androidx.fragment.app.FragmentTransaction
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
        it.onBackPressedDispatcher.onBackPressed()
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
        val transaction = activity.supportFragmentManager.beginTransaction()
        if (addToBackStack)
            transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.replace(R.id.mainContainer, screen.fragment)
        transaction.commit()
    }
}
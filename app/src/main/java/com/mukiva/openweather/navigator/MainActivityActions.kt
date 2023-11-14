package com.mukiva.openweather.navigator

import com.mukiva.openweather.ui.MainActivity

typealias MainActivityAction = (MainActivity) -> Unit

class MainActivityActions {

    var mainActivity: MainActivity? = null
        set(value) {
            field = value
            if (value != null) {
                mActions.forEach { it(value) }
                mActions.clear()
            }
        }

    private val mActions = mutableListOf<MainActivityAction>()

    fun clear() = mActions.clear()

    operator fun invoke(action: MainActivityAction) {
        when(mainActivity) {
            null -> mActions += action
            else -> action(mainActivity!!)
        }
    }

}
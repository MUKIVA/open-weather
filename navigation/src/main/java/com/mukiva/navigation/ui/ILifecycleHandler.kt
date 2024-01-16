package com.mukiva.navigation.ui

import androidx.fragment.app.FragmentActivity

interface ILifecycleHandler {

    fun onCreated(activity: FragmentActivity)
    fun onStart()
    fun onPause()
    fun onDestroy()
    fun onSaveInstanceState()
    fun onRestoreInstanceState()

}
package com.mukiva.navigation.ui

import androidx.fragment.app.FragmentActivity

abstract class AbstractLifecycleHandler : ILifecycleHandler {
    override fun onCreated(activity: FragmentActivity) {}
    override fun onDestroy() {}
    override fun onPause() {}
    override fun onStart() {}
    override fun onRestoreInstanceState() {}
    override fun onSaveInstanceState() {}
}
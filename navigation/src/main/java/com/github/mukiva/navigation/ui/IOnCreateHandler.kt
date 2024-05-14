package com.github.mukiva.navigation.ui

import androidx.fragment.app.FragmentActivity

interface IOnCreateHandler : ILifecycleHandler {
    fun onCreated(activity: FragmentActivity)
}

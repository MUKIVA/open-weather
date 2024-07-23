package com.github.mukiva.navigation.ui

import androidx.fragment.app.FragmentActivity

public interface IOnCreateHandler : ILifecycleHandler {
    public fun onCreated(activity: FragmentActivity)
}

package com.github.mukiva.feature.splash.ui

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.mukiva.feature.splash.R
import com.github.mukiva.feature.splash.presentation.OnboardingScreen

class OnboardingAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = OnboardingScreen.entries.size

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            OnboardingScreen.NOTIFICATION_ACCESS.ordinal ->
                GetAccessTemplateFragment
                    .newInstance(
                        GetAccessTemplateFragment.Args(
                            imageRes = R.drawable.ic_notifications,
                            description = R.string.post_notifications_description,
                            permission = Manifest.permission.POST_NOTIFICATIONS
                        )
                    )
            OnboardingScreen.LOCATION_ACCESS.ordinal ->
                GetAccessTemplateFragment
                    .newInstance(
                        GetAccessTemplateFragment.Args(
                            imageRes = R.drawable.ic_location,
                            description = R.string.location_description,
                            permission = Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
            else -> error("Unimplemented branch")
        }
    }
}

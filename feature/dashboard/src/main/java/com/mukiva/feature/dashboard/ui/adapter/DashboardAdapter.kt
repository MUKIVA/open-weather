package com.mukiva.feature.dashboard.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.mukiva.feature.dashboard.ui.DashboardTemplateFragment

class DashboardAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private var mLocationCount = 0

    override fun getItemCount(): Int = mLocationCount

    override fun createFragment(position: Int): Fragment {
        return DashboardTemplateFragment.newInstance(position)
    }

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    fun submit(count: Int) {
        mLocationCount = count
    }
}
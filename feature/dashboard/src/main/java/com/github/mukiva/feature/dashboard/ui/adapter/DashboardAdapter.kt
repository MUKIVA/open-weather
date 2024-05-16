package com.github.mukiva.feature.dashboard.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.mukiva.feature.dashboard.domain.model.Location
import com.github.mukiva.feature.dashboard.ui.DashboardTemplateFragment

class DashboardAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val mDiffer: AsyncListDiffer<Location> = AsyncListDiffer(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder(LocationDiffCallback).build()
    )

    override fun getItemCount(): Int = mDiffer.currentList.size

    override fun createFragment(position: Int): Fragment {
        val location = mDiffer.currentList[position]
        val fragment = DashboardTemplateFragment
            .newInstance(
                DashboardTemplateFragment.Args(
                    locationName = location.name,
                    region = location.region,
                )
            )
        return fragment
    }

    fun submit(list: List<Location>) {
        mDiffer.submitList(list)
    }
}
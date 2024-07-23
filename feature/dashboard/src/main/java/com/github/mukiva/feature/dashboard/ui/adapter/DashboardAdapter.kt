package com.github.mukiva.feature.dashboard.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.mukiva.feature.dashboard.domain.model.Location
import com.github.mukiva.feature.dashboard.ui.dashboard_template_fragment.DashboardTemplateFragment

internal class DashboardAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val mDiffer: AsyncListDiffer<Location> = AsyncListDiffer(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder(LocationDiffCallback).build()
    )

    override fun getItemCount(): Int = mDiffer.currentList.size

    override fun getItemId(position: Int): Long {
        return mDiffer.currentList[position].id
    }

    override fun containsItem(itemId: Long): Boolean {
        val location = mDiffer.currentList.firstOrNull { it.id == itemId }
        return location != null
    }

    override fun createFragment(position: Int): Fragment {
        val location = mDiffer.currentList[position]
        val fragment = DashboardTemplateFragment
            .newInstance(
                DashboardTemplateFragment.Args(location.id)
            )
        return fragment
    }
    operator fun get(pos: Int): Location = mDiffer.currentList[pos]

    fun submitList(list: List<Location>) {
        mDiffer.submitList(list)
    }
}

package com.mukiva.feature.forecast.ui.adapter.forecast

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mukiva.feature.forecast.presentation.HourlyForecast
import com.mukiva.feature.forecast.ui.GroupsTemplateFragment

class HourlyForecastAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val mDiffer: AsyncListDiffer<HourlyForecast> = AsyncListDiffer(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder(HourlyForecastDiffUtilCallback).build()
    )

    override fun getItemCount(): Int = mDiffer.currentList.size

    override fun createFragment(position: Int): Fragment {
        return GroupsTemplateFragment.newInstance(
            args = GroupsTemplateFragment
                .Args(position = position)
        )
    }

    operator fun get(pos: Int): HourlyForecast = mDiffer.currentList[pos]

    fun submit(days: List<HourlyForecast>) {
        mDiffer.submitList(days)
    }
}
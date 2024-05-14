package com.github.mukiva.feature.forecast.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.mukiva.feature.forecast.presentation.HourlyForecast
import com.github.mukiva.feature.forecast.ui.ForecastTimelineFragment

class HourlyForecastAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val mDiffer: AsyncListDiffer<HourlyForecast.Content> = AsyncListDiffer(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder(HourlyForecastDiffUtilCallback).build()
    )

    override fun getItemCount(): Int = mDiffer.currentList.size

    override fun createFragment(position: Int): Fragment {
        return ForecastTimelineFragment.newInstance(
            args = ForecastTimelineFragment
                .Args(position = position)
        )
    }

    operator fun get(pos: Int): HourlyForecast.Content = mDiffer.currentList[pos]

    fun submit(days: List<HourlyForecast.Content>) {
        mDiffer.submitList(days)
    }
}

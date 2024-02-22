package com.mukiva.feature.forecast.ui.adapter.forecast

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mukiva.feature.forecast.domain.IHourlyForecast
import com.mukiva.feature.forecast.domain.UnitsType
import com.mukiva.feature.forecast.ui.GroupsTemplateFragment

class HourlyForecastAdapter(
    fragment: Fragment,
    private val unitsTypeProvider: () -> UnitsType
) : FragmentStateAdapter(fragment) {

    private var mDayForecastStateList: Collection<IHourlyForecast> = emptyList()

    override fun getItemCount(): Int = mDayForecastStateList.size

    override fun createFragment(position: Int): Fragment {
        return GroupsTemplateFragment.newInstance(
            args = GroupsTemplateFragment
                .Args(
                    groups = mDayForecastStateList.elementAt(position).groups,
                    unitsType = unitsTypeProvider()
                )
        )
    }

    operator fun get(index: Int) = mDayForecastStateList.elementAt(index)

    fun submit(days: Collection<IHourlyForecast>) {
        val diffCallback = HourlyForecastDiffUtilCallback(mDayForecastStateList, days)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mDayForecastStateList = days
        diffResult.dispatchUpdatesTo(this)
    }
}
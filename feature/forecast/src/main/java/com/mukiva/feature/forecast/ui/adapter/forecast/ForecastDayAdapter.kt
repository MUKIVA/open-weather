package com.mukiva.feature.forecast.ui.adapter.forecast

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mukiva.feature.forecast.presentation.DayForecastState

class ForecastDayAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    private var mDayForecastStateList: List<DayForecastState> = emptyList()

    override fun getItemCount(): Int = mDayForecastStateList.size

    override fun createFragment(position: Int): Fragment {
        return Fragment()
    }

    operator fun get(index: Int) = mDayForecastStateList[index]

    fun submit(days: List<DayForecastState>) {
        val diffCallback = ForecastDayDiffUtilCallback(mDayForecastStateList, days)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mDayForecastStateList = days
        diffResult.dispatchUpdatesTo(this)
    }
}
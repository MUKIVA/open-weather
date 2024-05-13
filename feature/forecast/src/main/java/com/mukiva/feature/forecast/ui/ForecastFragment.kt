package com.mukiva.feature.forecast.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mukiva.core.ui.getArgs
import com.mukiva.core.ui.getAttrColor
import com.mukiva.core.ui.uiLazy
import com.mukiva.core.ui.viewBindings
import com.mukiva.feature.forecast.R
import com.mukiva.feature.forecast.databinding.FragmentForecastBinding
import com.mukiva.feature.forecast.databinding.ItemDayTabBinding
import com.mukiva.feature.forecast.presentation.ForecastState
import com.mukiva.feature.forecast.presentation.ForecastViewModel
import com.mukiva.feature.forecast.presentation.HourlyForecast
import com.mukiva.feature.forecast.ui.adapter.forecast.HourlyForecastAdapter
import com.mukiva.openweather.ui.error
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.openweather.ui.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DayOfWeekNames
import java.io.Serializable
import com.mukiva.core.ui.R as CoreUiRes

@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {

    data class Args(
        val locationName: String,
        val dayPosition: Int
    ) : Serializable

    private val mBinding by viewBindings(FragmentForecastBinding::bind)
    private val mViewModel by viewModels<ForecastViewModel>()
    private val mHourlyForecastAdapter by uiLazy {
        HourlyForecastAdapter(childFragmentManager, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAppbar()
        initViewPager()

        subscribeOnViewModel()
    }

    private fun initAppbar() = with(mBinding) {
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(toolbar)
    }

    private fun initViewPager() = with(mBinding) {
        val dayFormatter = LocalDateTime.Format {
            dayOfMonth()
        }

        val dayOfWeekFormatter = LocalDateTime.Format {
            dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        }

        viewPager.offscreenPageLimit = 2
        viewPager.adapter = mHourlyForecastAdapter

        TabLayoutMediator(tabLayout, viewPager, true, true) { tab, index ->
            val inflater = LayoutInflater.from(requireContext())
            val view = ItemDayTabBinding.inflate(inflater, tabLayout, false)
            tab.customView = view.root

            view.day.text = dayFormatter.format(mHourlyForecastAdapter[index].date)
            view.dayOfWeek.text = dayOfWeekFormatter.format(mHourlyForecastAdapter[index].date)
        }.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val deselectedTextColor =
                    getAttrColor(com.google.android.material.R.attr.colorPrimary)
                val selectedTextColor =
                    getAttrColor(com.google.android.material.R.attr.colorSurface)

                for (i in 0 until tabLayout.tabCount) {
                    val tab = tabLayout.getTabAt(i)
                    val view = tab?.customView ?: continue
                    val bind = ItemDayTabBinding.bind(view)

                    if (i == position) {
                        bind.dayOfWeek.background = AppCompatResources
                            .getDrawable(requireContext(), R.drawable.background_day_tab)
                        bind.dayOfWeek.setTextColor(selectedTextColor)
                    } else {
                        bind.dayOfWeek.setBackgroundColor(Color.TRANSPARENT)
                        bind.dayOfWeek.setTextColor(deselectedTextColor)
                    }
                }
            }
        })
    }

    private fun subscribeOnViewModel() {
        mViewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach(::updateState)
            .launchIn(lifecycleScope)
    }

    private fun updateState(state: ForecastState) {
        if (state is ForecastState.Content) {
            updateViewPager(state.hourlyForecast)
        }
        updateType(state)
    }

    private fun updateViewPager(list: List<HourlyForecast.Content>) {
        mHourlyForecastAdapter.submit(list)
    }

    private fun updateType(state: ForecastState) = with(mBinding) {
        when (state) {
            is ForecastState.Content -> {
                emptyView.hide()
                content.visible()
                viewPager.setCurrentItem(getArgs(Args::class.java).dayPosition, false)
            }
            ForecastState.Error -> {
                content.gone()
                emptyView.error(
                    getString(CoreUiRes.string.error_msg),
                    getString(CoreUiRes.string.refresh)
                ) {
                    mViewModel.loadForecast(getArgs(Args::class.java).locationName)
                }
            }
            ForecastState.Init -> {
                content.gone()
                emptyView.loading()
                mViewModel.loadForecast(getArgs(Args::class.java).locationName)
            }
            ForecastState.Loading -> {
                emptyView.loading()
                content.gone()
            }
        }
    }
}

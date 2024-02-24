package com.mukiva.feature.forecast.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mukiva.core.ui.KEY_ARGS
import com.mukiva.core.ui.getArgs
import com.mukiva.core.ui.getAttrColor
import com.mukiva.feature.forecast.R
import com.mukiva.feature.forecast.databinding.FragmentForecastBinding
import com.mukiva.feature.forecast.presentation.ForecastState
import com.mukiva.feature.forecast.presentation.ForecastViewModel
import com.mukiva.feature.forecast.ui.adapter.forecast.HourlyForecastAdapter
import com.mukiva.openweather.ui.error
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.core.ui.uiLazy
import com.mukiva.core.ui.viewBindings
import com.mukiva.feature.forecast.databinding.ItemDayTabBinding
import com.mukiva.feature.forecast.domain.IHourlyForecast
import com.mukiva.feature.forecast.domain.UnitsType
import com.mukiva.openweather.ui.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable
import java.text.SimpleDateFormat


@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {

    data class Args(
        val locationName: String,
        val dayPosition: Int
    ) : Serializable

    private var mUnitsTypeProvider: () -> UnitsType = { UnitsType.METRIC }
    private val mBinding by viewBindings(FragmentForecastBinding::bind)
    private val mViewModel by viewModels<ForecastViewModel>()
    private val mHourlyForecastAdapter by uiLazy {
        HourlyForecastAdapter(
            fragment = this,
            unitsTypeProvider = { mUnitsTypeProvider() },
            locationName = getArgs(Args::class.java).locationName
        )
    }
    private var mDayIsInit: Boolean = false


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
        val dayFormatter = SimpleDateFormat(
            "EEE",
            requireContext().resources.configuration.locales[0]
        )

        val dayOfWeekFormatter = SimpleDateFormat(
            "d",
            requireContext().resources.configuration.locales[0]
        )

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

                val deselectedTextColor = getAttrColor(com.google.android.material.R.attr.colorPrimary)
                val selectedTextColor = getAttrColor(com.google.android.material.R.attr.colorSurface)


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
        lifecycleScope.launch {
            mViewModel.state
                .flowWithLifecycle(lifecycle)
                .collect(::updateState)
        }
    }

    private fun updateState(state: ForecastState) {
        mUnitsTypeProvider = { state.unitsType }
        updateViewPager(state.hourlyForecast)
        updateType(state.type)
    }

    private fun updateViewPager(list: Collection<IHourlyForecast>) {
        mHourlyForecastAdapter.submit(list)
    }

    private fun updateType(type: ForecastState.Type) = with(mBinding) {
        when(type) {
            ForecastState.Type.INIT -> {
                content.gone()
                emptyView.loading()
                mViewModel.loadForecast(getArgs(Args::class.java).locationName)
            }
            ForecastState.Type.ERROR -> {
                content.gone()
                emptyView.error(
                    getString(R.string.get_forecast_error),
                    getString(R.string.refresh)
                ) {
                    mViewModel.loadForecast(getArgs(Args::class.java).locationName)
                }
            }
            ForecastState.Type.CONTENT -> {
                if (!mDayIsInit) {
                    viewPager.setCurrentItem(getArgs(Args::class.java).dayPosition, false)
                    mDayIsInit = true
                }
                emptyView.hide()
                content.visible()
            }
            ForecastState.Type.LOADING -> {
                emptyView.loading()
                content.gone()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_DAY_IS_INIT, mDayIsInit)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) return
        mDayIsInit = savedInstanceState.getBoolean(KEY_DAY_IS_INIT)
    }

    companion object {

        private const val KEY_DAY_IS_INIT = "KEY_DAY_IS_INIT"

        fun newInstance(args: Args) = ForecastFragment().apply {
            arguments = bundleOf(
                KEY_ARGS to args
            )
        }
    }
}
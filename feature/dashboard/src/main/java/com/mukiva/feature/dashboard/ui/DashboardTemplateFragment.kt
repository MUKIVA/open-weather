package com.mukiva.feature.dashboard.ui

import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.mukiva.core.ui.KEY_ARGS
import com.mukiva.core.ui.getArgs
import com.mukiva.feature.dashboard.R
import com.mukiva.feature.dashboard.databinding.FragmentDashboardTemplateBinding
import com.mukiva.feature.dashboard.domain.model.ICondition
import com.mukiva.feature.dashboard.domain.model.ICurrentWeather
import com.mukiva.feature.dashboard.domain.model.UnitsType
import com.mukiva.feature.dashboard.domain.model.WindDirection
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.core.ui.uiLazy
import com.mukiva.core.ui.viewBindings
import com.mukiva.feature.dashboard.domain.model.IMinimalForecast
import com.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.mukiva.feature.dashboard.presentation.IDashboardState
import com.mukiva.feature.dashboard.presentation.MinorWeatherState
import com.mukiva.feature.dashboard.ui.adapter.MinimalForecastAdapter
import com.mukiva.openweather.ui.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint
class DashboardTemplateFragment : Fragment(R.layout.fragment_dashboard_template) {

    data class Args(
        val position: Int
    ): Serializable

    private val mBinding by viewBindings(FragmentDashboardTemplateBinding::bind)
    private val mViewModel by viewModels<DashboardViewModel>(
        ownerProducer = { requireParentFragment() }
    )
    private val mMinimalForecastAdapter by uiLazy {
        MinimalForecastAdapter(
            onItemClick = { pos -> mViewModel.goForecast(pos) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.emptyView.root
            .layoutTransition
            .setAnimateParentHierarchy(false)

        initNestedScroll()
        initForecastList()
        subscribeOnViewModel()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) = with(mBinding) {
        super.onViewStateRestored(savedInstanceState)
        content.scrollY = ScrollSynchronizer.getLastScroll()
    }

    private fun initForecastList() = with(mBinding) {
        daysForecastList.adapter = mMinimalForecastAdapter
    }

    private fun initNestedScroll() = with(mBinding) {
        content.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            ScrollSynchronizer.updateScroll(scrollY, lifecycleScope)
        }

        ViewCompat.setOnApplyWindowInsetsListener(grid) { v, insets ->

            val navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            v.updatePadding(
                bottom = navInsets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }

        lifecycleScope.launch {
            ScrollSynchronizer.getOffsetFlow()
                .flowWithLifecycle(lifecycle)
                .collect { content.scrollY = it }
        }
    }

    private fun subscribeOnViewModel() {
        mViewModel.observeState(IDashboardState.MinorState::class, viewLifecycleOwner) {
            val pos = getArgs(Args::class.java).position
            val state = it.list.elementAt(pos)
            updateFragmentType(state.type)
            if (state.currentWeather == null) return@observeState
            with(state.currentWeather) {
                updateDayStatus(isDay)
                updateFeelsLike(this, UnitsType.METRIC)
                updateConditionField(condition, cloud)
                updateWindSpeed(this, UnitsType.METRIC)
                updateWindDirection(windDir, windDegree.toFloat())
                updateHumidity(humidity)
                updatePressure(this, UnitsType.METRIC)
            }
            updateForecast(state.minimalForecastState)
        }
    }

    private fun updateForecast(state: Collection<IMinimalForecast>) {
        mMinimalForecastAdapter.submitList(state.toList())
    }

    private fun updatePressure(
        currentWeather: ICurrentWeather,
        unitsType: UnitsType
    ) = with(mBinding.pressureField) {
        subtitle.text = getString(R.string.field_pressure_subtitle)

        val pressureMmHg = currentWeather.pressureMb * FROM_MBAR_TO_MMHG

        when(unitsType) {
            UnitsType.METRIC -> {
                fieldValue.text = getString(
                    R.string.template_mmhg,
                    pressureMmHg.toInt().toString()
                )
            }
            UnitsType.IMPERIAL -> {
                fieldValue.text = getString(
                    R.string.template_mb,
                    currentWeather.pressureMb.toInt().toString()
                )
            }
        }
    }

    private fun updateHumidity(humidity: Int) = with(mBinding.humidityField) {
        subtitle.text = getString(R.string.field_humidity_subtitle)
        fieldValue.text = getString(R.string.template_percent, humidity.toString())
        fieldValue.setDrawable(R.drawable.ic_humidity)
    }

    private fun updateWindDirection(dir: WindDirection, degree: Float) = with(mBinding.windDirField) {
        subtitle.text = getString(R.string.field_wind_dir_subtitle)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_dir)
        val rotatedDrawable = RotateDrawable().apply {
            fromDegrees = 0f
            toDegrees = degree
            level = 10000
            setDrawable(drawable)
        }

        fieldValue.setDrawable(rotatedDrawable)
        fieldValue.text = dir.name
    }

    private fun updateWindSpeed(
        currentWeather: ICurrentWeather,
        speedUnitsType: UnitsType
    ) = with(mBinding.windSpeedField) {
        subtitle.text = getString(R.string.field_wind_speed_subtitle)
        when(speedUnitsType) {
            UnitsType.METRIC -> {
                fieldValue.text = getString(R.string.template_kmh, currentWeather.windKph.toInt())
            }
            UnitsType.IMPERIAL -> {
                fieldValue.text = getString(R.string.template_mph, currentWeather.windMph.toInt())
            }
        }
        fieldValue.setDrawable(R.drawable.ic_wind_speed)

    }

    private fun updateConditionField(
        condition: ICondition,
        cloudPercent: Int
    ) = with(mBinding.conditionField) {
        subtitle.text = getString(R.string.field_condition_subtitle)
        fieldValue.text = getString(R.string.template_cloud, cloudPercent, condition.text)
        fieldValue.setDrawable(R.drawable.ic_condition)
    }

    private fun updateFeelsLike(currentWeather: ICurrentWeather, unitsType: UnitsType) = with(mBinding.feelsLikeField) {
        val value = when(unitsType) {
            UnitsType.METRIC -> getString(
                R.string.template_celsius_main_card,
                currentWeather.feelsLikeC.toInt()
            )
            UnitsType.IMPERIAL -> getString(
                R.string.template_fahrenheit_main_card,
                currentWeather.feelsLikeF.toInt()
            )
        }

        subtitle.text = getString(R.string.field_feels_like_subtitle)
        fieldValue.text = value
        fieldValue.setDrawable(R.drawable.ic_feels_like)
    }

    private fun updateFragmentType(type: MinorWeatherState.Type) = with(mBinding) {
        when(type) {
            MinorWeatherState.Type.LOADING -> {
                content.gone()
                emptyView.loading()
            }
            MinorWeatherState.Type.CONTENT -> {
                content.visible()
                emptyView.hide()
            }
        }
    }

    private fun updateDayStatus(isDay: Boolean) = with(mBinding.isDayField) {
        subtitle.text = getString(R.string.field_is_day_subtitle)
        when (isDay) {
            true -> {
                fieldValue.text = getString(R.string.is_day_true)
                fieldValue.setDrawable(R.drawable.ic_day)
            }
            false -> {
                fieldValue.text = getString(R.string.is_day_false)
                fieldValue.setDrawable(R.drawable.ic_night)
            }
        }
    }

    private fun TextView.setDrawable(@DrawableRes res: Int) {
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, res, 0)
    }

    private fun TextView.setDrawable(drawable: Drawable) {
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
    }

    companion object {
        private const val FROM_MBAR_TO_MMHG = 0.750062f

        fun newInstance(args: Args) = DashboardTemplateFragment().apply {
            arguments = bundleOf(
                KEY_ARGS to args
            )
        }
    }

}
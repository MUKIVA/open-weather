package com.ui

import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.domain.model.Condition
import com.domain.model.CurrentWeather
import com.domain.model.WindDirection
import com.mukiva.api.UnitsType
import com.mukiva.feature.dashboard_impl.R
import com.mukiva.feature.dashboard_impl.databinding.FragmentDashboardTemplateBinding
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.openweather.ui.uiLazy
import com.mukiva.openweather.ui.viewBindings
import com.mukiva.openweather.ui.visible
import com.presentation.AdditionalDashboardInfoViewModel
import com.presentation.AdditionalInfoState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardTemplateFragment : Fragment(R.layout.fragment_dashboard_template) {

    private val mBinding by viewBindings(FragmentDashboardTemplateBinding::bind)
    private val mViewModel by viewModels<AdditionalDashboardInfoViewModel>()
    private val mLocationPosition by uiLazy {
        requireArguments().getInt(ARG_LOCATION_POSITION)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeOnViewModel()
    }

    private fun subscribeOnViewModel() {
        mViewModel.loadPosition(mLocationPosition)
        lifecycleScope.launch {
            mViewModel.state
                .flowWithLifecycle(lifecycle)
                .collect { state ->
                    updateState(state)
                }
        }

    }

    private fun updateState(state: AdditionalInfoState) {
        updateFragmentType(state.type)
        if (state.currentWeather == null) return
        with(state.currentWeather) {
            updateDayStatus(isDay)
            updateFeelsLike(this, state.unitsType)
            updateConditionField(condition, cloud)
            updateWindSpeed(this, state.unitsType)
            updateWindDirection(windDir, windDegree.toFloat())
            updateHumidity(humidity)
            updatePressure(this, state.unitsType)
        }
    }

    private fun updatePressure(
        currentWeather: CurrentWeather,
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
        currentWeather: CurrentWeather,
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
        condition: Condition,
        cloudPercent: Int
    ) = with(mBinding.conditionField) {
        subtitle.text = getString(R.string.field_condition_subtitle)
        fieldValue.text = getString(R.string.template_cloud, cloudPercent, condition.text)
        fieldValue.setDrawable(R.drawable.ic_condition)
    }

    private fun updateFeelsLike(currentWeather: CurrentWeather, unitsType: UnitsType) = with(mBinding.feelsLikeField) {
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

    private fun updateFragmentType(type: AdditionalInfoState.Type) = with(mBinding) {
        when(type) {
            AdditionalInfoState.Type.LOADING -> {
                content.gone()
                emptyView.loading()
            }
            AdditionalInfoState.Type.CONTENT -> {
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

        private const val ARG_LOCATION_POSITION = "ARG_LOCATION_POSITION"

        private const val FROM_MBAR_TO_MMHG = 0.750062f

        fun newInstance(position: Int) = DashboardTemplateFragment().apply {
            arguments = bundleOf(
                ARG_LOCATION_POSITION to position
            )
        }
    }

}
package com.github.mukiva.feature.dashboard.ui

import android.graphics.Rect
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
import androidx.recyclerview.widget.RecyclerView
import com.github.mukiva.core.ui.KEY_ARGS
import com.github.mukiva.core.ui.error
import com.github.mukiva.core.ui.getArgs
import com.github.mukiva.core.ui.getDimen
import com.github.mukiva.core.ui.getPressureString
import com.github.mukiva.core.ui.getSpeedString
import com.github.mukiva.core.ui.getTempString
import com.github.mukiva.core.ui.gone
import com.github.mukiva.core.ui.hide
import com.github.mukiva.core.ui.lazyAdapter
import com.github.mukiva.core.ui.loading
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.core.ui.visible
import com.github.mukiva.feature.dashboard.R
import com.github.mukiva.feature.dashboard.databinding.FragmentDashboardTemplateBinding
import com.github.mukiva.feature.dashboard.presentation.LocationWeatherState
import com.github.mukiva.feature.dashboard.presentation.SharedDashboardViewModel
import com.github.mukiva.feature.dashboard.ui.adapter.MinimalForecastAdapter
import com.github.mukiva.openweather.core.domain.weather.Pressure
import com.github.mukiva.openweather.core.domain.weather.Speed
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.openweather.core.domain.weather.WindDirection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.Serializable
import com.github.mukiva.core.ui.R as CoreUiRes

@AndroidEntryPoint
class DashboardTemplateFragment :
    Fragment(R.layout.fragment_dashboard_template) {

    data class Args(
        val locationName: String,
        val region: String,
    ) : Serializable

    private val mBinding by viewBindings(FragmentDashboardTemplateBinding::bind)
    private val mViewModel by viewModels<SharedDashboardViewModel>(
        ownerProducer = { requireParentFragment() }
    )
    private val mMinimalForecastAdapter by lazyAdapter {
        MinimalForecastAdapter(
            onItemClick = { pos ->
                mViewModel.goFullForecast(getArgs(Args::class.java).locationName, pos)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initForecastList()
        subscribeOnViewModel()
    }

    private fun initForecastList() = with(mBinding) {
        daysForecastList.adapter = mMinimalForecastAdapter
        daysForecastList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = getDimen(CoreUiRes.dimen.def_v_padding)
            }
        })
    }

    private fun subscribeOnViewModel() {
        mViewModel.getForecastStateHolder(getArgs(Args::class.java).locationName)
            .forecastState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach(::updateState)
            .launchIn(lifecycleScope)
    }

    private fun updateContent(state: LocationWeatherState.Content) {
        val forecast = state.forecast
        val current = forecast.currentWeather
        updateForecast(forecast.forecastState)
        updateHumidity(current.humidity)
        updatePressure(current.pressure)
        updateWindDirection(current.windDir, current.windDegree.toFloat())
        updateConditionField(current.condition, current.cloud)
        updateDayStatus(current.isDay)
        updateFeelsLike(current.feelsLike)
        updateWindSpeed(current.windSpeed)
    }

    private fun updateForecast(state: Collection<com.github.mukiva.feature.dashboard.domain.model.MinimalForecast>) {
        mMinimalForecastAdapter.submitList(state.toList())
    }

    private fun updatePressure(
        pressure: Pressure
    ) = with(mBinding.pressureField) {
        subtitle.text = getString(R.string.field_pressure_subtitle)
        fieldValue.text = getPressureString(pressure)
    }

    private fun updateHumidity(humidity: Int) = with(mBinding.humidityField) {
        subtitle.text = getString(R.string.field_humidity_subtitle)
        fieldValue.text = getString(CoreUiRes.string.template_percent, humidity)
        fieldValue.setDrawable(CoreUiRes.drawable.ic_humidity)
    }

    private fun updateWindDirection(dir: WindDirection, degree: Float) = with(mBinding.windDirField) {
        subtitle.text = getString(R.string.field_wind_dir_subtitle)
        val drawable = ContextCompat.getDrawable(requireContext(), CoreUiRes.drawable.ic_dir)
        val rotatedDrawable = RotateDrawable().apply {
            fromDegrees = 0f
            toDegrees = degree
            level = ROTATE_DRAWABLE_MAX_LEVEL
            setDrawable(drawable)
        }

        fieldValue.setDrawable(rotatedDrawable)
        fieldValue.text = dir.name
    }

    private fun updateWindSpeed(
        windSpeed: Speed,
    ) = with(mBinding.windSpeedField) {
        subtitle.text = getString(R.string.field_wind_speed_subtitle)
        fieldValue.text = getSpeedString(windSpeed)
        fieldValue.setDrawable(CoreUiRes.drawable.ic_wind_speed)
    }

    private fun updateConditionField(
        condition: com.github.mukiva.feature.dashboard.domain.model.Condition,
        cloudPercent: Int
    ) = with(mBinding.conditionField) {
        subtitle.text = getString(R.string.field_condition_subtitle)
        fieldValue.text = getString(R.string.template_cloud, cloudPercent, condition.text)
        fieldValue.setDrawable(CoreUiRes.drawable.ic_condition)
    }

    private fun updateFeelsLike(temp: Temp) = with(mBinding.feelsLikeField) {
        subtitle.text = getString(R.string.field_feels_like_subtitle)
        fieldValue.text = getTempString(temp)
        fieldValue.setDrawable(CoreUiRes.drawable.ic_feels_like)
    }

    private fun updateState(state: LocationWeatherState) {
        val loadAction = {
            val locationName = getArgs(Args::class.java).locationName
            mViewModel
                .getForecastStateHolder(locationName)
                .loadForecast(locationName)
        }

        when (state) {
            is LocationWeatherState.Content -> with(mBinding) {
                emptyView.hide()
                root.visible()
                updateContent(state)
            }
            LocationWeatherState.Error -> with(mBinding) {
                emptyView.error(
                    msg = getString(CoreUiRes.string.error_msg),
                    buttonText = getString(CoreUiRes.string.refresh),
                    onButtonClick = loadAction
                )
                root.gone()
            }
            LocationWeatherState.Init -> with(mBinding) {
                emptyView.loading()
                root.gone()
                loadAction()
            }
            LocationWeatherState.Loading -> with(mBinding) {
                emptyView.loading()
                root.gone()
            }
        }
    }

    private fun updateDayStatus(isDay: Boolean) = with(mBinding.isDayField) {
        subtitle.text = getString(R.string.field_is_day_subtitle)
        when (isDay) {
            true -> {
                fieldValue.text = getString(R.string.is_day_true)
                fieldValue.setDrawable(CoreUiRes.drawable.ic_day)
            }
            false -> {
                fieldValue.text = getString(R.string.is_day_false)
                fieldValue.setDrawable(CoreUiRes.drawable.ic_night)
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
        private const val ROTATE_DRAWABLE_MAX_LEVEL = 10000

        fun newInstance(args: Args) = DashboardTemplateFragment().apply {
            arguments = bundleOf(
                KEY_ARGS to args
            )
        }
    }
}

package com.mukiva.feature.forecast.ui.adapter.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mukiva.core.ui.view.IValueProvider
import com.mukiva.feature.forecast.databinding.ItemHumidityBinding
import com.mukiva.feature.forecast.databinding.ItemPressureBinding
import com.mukiva.feature.forecast.databinding.ItemTempBinding
import com.mukiva.feature.forecast.databinding.ItemWindBinding
import com.mukiva.feature.forecast.domain.ForecastItem
import com.mukiva.feature.forecast.domain.UnitsType
import kotlin.math.max
import kotlin.math.min

class ForecastItemAdapter(
    private val unitsType: () -> UnitsType,
) : ListAdapter<ForecastItem, ForecastItemViewHolder>(ForecastItemDiffUtil)
    , IValueProvider
{
    private var mMaxValue: Float = Float.MAX_VALUE
    private var mMinValue: Float = Float.MIN_VALUE
    private val mSubmitListeners = mutableListOf<() -> Unit>()

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is ForecastItem.HourlyHumidity -> HUMIDITY_VIEW_TYPE
            is ForecastItem.HourlyPressure -> PRESSURE_VIEW_TYPE
            is ForecastItem.HourlyTemp -> TEMP_VIEW_TYPE
            is ForecastItem.HourlyWind -> WIND_VIEW_TYPE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HUMIDITY_VIEW_TYPE -> ForecastItemViewHolder.HumidityItemViewHolder(
                ItemHumidityBinding.inflate(inflater, parent, false),
                unitsType()
            )
            PRESSURE_VIEW_TYPE -> ForecastItemViewHolder.PressureItemViewHolder(
                ItemPressureBinding.inflate(inflater, parent, false),
                unitsType()
            )
            TEMP_VIEW_TYPE -> ForecastItemViewHolder.TempItemViewHolder(
                ItemTempBinding.inflate(inflater, parent, false),
                unitsType()
            )
            WIND_VIEW_TYPE -> ForecastItemViewHolder.WindItemViewHolder(
                ItemWindBinding.inflate(inflater, parent, false),
                unitsType()
            )
            else -> error("Fail to get ViewType")
        }
    }

    override fun onBindViewHolder(holder: ForecastItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private const val HUMIDITY_VIEW_TYPE = 0
        private const val PRESSURE_VIEW_TYPE = 1
        private const val TEMP_VIEW_TYPE = 2
        private const val WIND_VIEW_TYPE = 3
    }

    override fun getValueByPos(pos: Int): Float {
        return when(val item = getItem(pos)) {
            is ForecastItem.HourlyHumidity -> item.humidity.toFloat()
            is ForecastItem.HourlyPressure -> item.pressureIn.toFloat()
            is ForecastItem.HourlyTemp -> item.tempC.toFloat()
            is ForecastItem.HourlyWind -> item.windKph.toFloat()
        }
    }

    override fun getMaxValue(): Float {
        return mMaxValue
    }

    override fun getMinValue(): Float {
        return mMinValue
    }

    override fun registerOnListSubmitted(block: () -> Unit) {
        mSubmitListeners.add(block)

        if (itemCount != 0)
            block()
    }

    override fun getFormattedValue(index: Int): String {
        return when (val item = getItem(index)) {
            is ForecastItem.HourlyHumidity -> item.humidity.toString()
            is ForecastItem.HourlyPressure -> item.pressureMb.toString()
            is ForecastItem.HourlyTemp -> item.tempC.toInt().toString()
            is ForecastItem.HourlyWind -> item.windKph.toInt().toString()
        }
    }

    override fun submitList(list: List<ForecastItem>?) {
        super.submitList(list)
        mMinValue = Float.MAX_VALUE
        mMaxValue = Float.MIN_VALUE
        list?.forEach {
            when (it) {
                is ForecastItem.HourlyHumidity -> {
                    mMaxValue = max(it.humidity.toFloat(), mMaxValue)
                    mMinValue = min(it.humidity.toFloat(), mMinValue)
                }
                is ForecastItem.HourlyPressure -> {
                    mMaxValue = max(it.pressureIn.toFloat(), mMaxValue)
                    mMinValue = min(it.pressureIn.toFloat(), mMinValue)
                }
                is ForecastItem.HourlyTemp -> {
                    mMaxValue = max(it.tempC.toFloat(), mMaxValue)
                    mMinValue = min(it.tempC.toFloat(), mMinValue)
                }
                is ForecastItem.HourlyWind -> {
                    mMaxValue = max(it.windKph.toFloat(), mMaxValue)
                    mMinValue = min(it.windKph.toFloat(), mMinValue)
                }
            }
        }
    }

}
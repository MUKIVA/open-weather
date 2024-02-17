package com.mukiva.feature.forecast.ui.adapter.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mukiva.core.ui.view.IValueProvider
import com.mukiva.feature.forecast.databinding.ItemHumidityBinding
import com.mukiva.feature.forecast.databinding.ItemPressureBinding
import com.mukiva.feature.forecast.databinding.ItemTempBinding
import com.mukiva.feature.forecast.databinding.ItemWindBinding
import com.mukiva.feature.forecast.domain.IForecastItem
import kotlin.math.max
import kotlin.math.min

class ForecastItemAdapter :
    ListAdapter<IForecastItem, ForecastItemViewHolder>(ForecastItemDiffUtil)
    , IValueProvider
{
    private var mMaxValue: Float = Float.MAX_VALUE
    private var mMinValue: Float = Float.MIN_VALUE
    private val mSubmitListeners = mutableListOf<() -> Unit>()

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is IForecastItem.IHourlyHumidity -> HUMIDITY_VIEW_TYPE
            is IForecastItem.IHourlyPressure -> PRESSURE_VIEW_TYPE
            is IForecastItem.IHourlyTemp -> TEMP_VIEW_TYPE
            is IForecastItem.IHourlyWind -> WIND_VIEW_TYPE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HUMIDITY_VIEW_TYPE -> ForecastItemViewHolder.HumidityItemViewHolder(ItemHumidityBinding
                .inflate(inflater, parent, false)
            )
            PRESSURE_VIEW_TYPE -> ForecastItemViewHolder.PressureItemViewHolder(ItemPressureBinding
                .inflate(inflater, parent, false)
            )
            TEMP_VIEW_TYPE -> ForecastItemViewHolder.TempItemViewHolder(ItemTempBinding
                .inflate(inflater, parent, false)
            )
            WIND_VIEW_TYPE -> ForecastItemViewHolder.WindItemViewHolder(ItemWindBinding
                .inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Fail to get ViewType")
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
        val item = getItem(pos)
        return when(item) {
            is IForecastItem.IHourlyHumidity -> item.humidity.toFloat()
            is IForecastItem.IHourlyPressure -> item.pressureIn.toFloat()
            is IForecastItem.IHourlyTemp -> item.tempC.toFloat()
            is IForecastItem.IHourlyWind -> item.windKph.toFloat()
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
        val item = getItem(index)
        return when (item) {
            is IForecastItem.IHourlyHumidity -> item.humidity.toString()
            is IForecastItem.IHourlyPressure -> item.pressureMb.toString()
            is IForecastItem.IHourlyTemp -> item.tempC.toInt().toString()
            is IForecastItem.IHourlyWind -> item.windKph.toInt().toString()
        }
    }

    override fun submitList(list: List<IForecastItem>?) {
        super.submitList(list)
        mMinValue = Float.MAX_VALUE
        mMaxValue = Float.MIN_VALUE
        list?.forEach {
            when (it) {
                is IForecastItem.IHourlyHumidity -> {
                    mMaxValue = max(it.humidity.toFloat(), mMaxValue)
                    mMinValue = min(it.humidity.toFloat(), mMinValue)
                }
                is IForecastItem.IHourlyPressure -> {
                    mMaxValue = max(it.pressureIn.toFloat(), mMaxValue)
                    mMinValue = min(it.pressureIn.toFloat(), mMinValue)
                }
                is IForecastItem.IHourlyTemp -> {
                    mMaxValue = max(it.tempC.toFloat(), mMaxValue)
                    mMinValue = min(it.tempC.toFloat(), mMinValue)
                }
                is IForecastItem.IHourlyWind -> {
                    mMaxValue = max(it.windKph.toFloat(), mMaxValue)
                    mMinValue = min(it.windKph.toFloat(), mMinValue)
                }
            }
        }
    }

}
package com.mukiva.feature.forecast.ui.adapter.forecast

import android.graphics.drawable.RotateDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mukiva.feature.forecast.R
import com.mukiva.core.ui.R as CoreUiRes
import com.mukiva.feature.forecast.databinding.ItemHumidityBinding
import com.mukiva.feature.forecast.databinding.ItemPressureBinding
import com.mukiva.feature.forecast.databinding.ItemTempBinding
import com.mukiva.feature.forecast.databinding.ItemWindBinding
import com.mukiva.feature.forecast.domain.IForecastItem
import com.mukiva.feature.forecast.domain.UnitsType
import com.mukiva.feature.forecast.domain.WindDirection
import java.util.Date
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

sealed class ForecastItemViewHolder(
    root: View,
    protected val unitsType: UnitsType
) : RecyclerView.ViewHolder(root) {

    protected val mTimeFormatter = SimpleDateFormat(
        "HH:mm",
        itemView.context.resources.configuration.locales[0]
    )

    abstract fun bind(item: IForecastItem)

    class TempItemViewHolder(
        private val bind: ItemTempBinding,
        unitsType: UnitsType
    ) : ForecastItemViewHolder(bind.root, unitsType) {
        override fun bind(item: IForecastItem) {
            if (item !is IForecastItem.IHourlyTemp) return
            updateTemp(item.tempC, item.tempF, unitsType)
            updateFeelsLike(item.feelsLikeC, item.feelsLikeF, unitsType)
            updateCloud(item.cloud)
            updateCloudIcon(item.iconUrl)
            updateTime(item.date)
        }

        private fun updateTemp(
            tempC: Double,
            tempF: Double,
            unitsType: UnitsType
        ) = with(bind) {
            val (strRes, temp) = asLocaleTempTuple(tempC, tempF, unitsType)
            tempValue.text = itemView.context.getString(strRes, temp)

        }

        private fun updateFeelsLike(
            tempC: Double,
            tempF: Double,
            unitsType: UnitsType
        ) = with(bind) {
            val (strRes, temp) = asLocaleTempTuple(tempC, tempF, unitsType)
            feelsLikeValue.text = itemView.context.getString(strRes, temp)
        }

        private fun updateCloud(cloud: Double) = with(bind) {
            cloudValue.text = itemView.context
                .getString(CoreUiRes.string.template_percent, cloud.roundToInt())
        }

        private fun updateCloudIcon(iconUrl: String) = with(bind) {
            Glide.with(root)
                .load("https:$iconUrl")
                .into(icon)
        }

        private fun updateTime(date: Date) = with(bind) {
            time.text = mTimeFormatter.format(date)
        }

        private fun asLocaleTempTuple(
            tempC: Double,
            tempF: Double,
            unitsType: UnitsType
        ) = when(unitsType) {
            UnitsType.METRIC -> Pair(
                CoreUiRes.string.template_celsius,
                tempC.roundToInt()
            )
            UnitsType.IMPERIAL -> Pair(
                CoreUiRes.string.template_fahrenheit,
                tempF.roundToInt()
            )
        }
    }

    class WindItemViewHolder(
        private val bind: ItemWindBinding,
        unitsType: UnitsType
    ) : ForecastItemViewHolder(bind.root, unitsType) {

        override fun bind(item: IForecastItem) {
            if (item !is IForecastItem.IHourlyWind) return

            updateWindDegree(item.windDegree)
            updateWindSpeed(item.windMph, item.windKph, unitsType)
            updateTime(item.date)
            updateIcon(item.windDegree, item.windDirection)

        }

        private fun updateWindSpeed(
            speedMph: Double,
            speedKph: Double,
            unitsType: UnitsType
        ) = with(bind) {

            val (strRes, speed) = when(unitsType) {
                UnitsType.METRIC -> Pair(CoreUiRes.string.template_kmh, speedKph)
                UnitsType.IMPERIAL -> Pair(CoreUiRes.string.template_mph, speedMph)
            }

            windSpeedValue.text = itemView.context
                .getString(strRes, speed.roundToInt())
        }

        private fun updateWindDegree(degree: Int) = with(bind) {
            degreeValue.text = itemView.context
                .getString(CoreUiRes.string.template_degree, degree)
        }

        private fun updateTime(time: Date) = with(bind) {
            this.time.text = mTimeFormatter.format(time)
        }

        private fun updateIcon(degree: Int, dir: WindDirection) = with(bind) {
            direction.text = dir.name
            val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_dir)
            val rotatedDrawable = RotateDrawable().apply {
                fromDegrees = 0f
                toDegrees = degree.toFloat()
                level = 10000
                setDrawable(drawable)
            }
            icon.setImageDrawable(rotatedDrawable)
        }
    }

    class HumidityItemViewHolder(
        private val bind: ItemHumidityBinding,
        unitsType: UnitsType
    ) : ForecastItemViewHolder(bind.root, unitsType) {
        override fun bind(item: IForecastItem) {
            if (item !is IForecastItem.IHourlyHumidity) return
            updateValue(item.humidity)
            updateTime(item.date)
        }

        private fun updateValue(value: Int) = with(bind) {
            this.value.text = itemView.context
                .getString(CoreUiRes.string.template_percent, value)
        }

        private fun updateTime(time: Date) = with(bind) {
            this.time.text = mTimeFormatter.format(time)
        }
    }

    class PressureItemViewHolder(
        private val bind: ItemPressureBinding,
        unitsType: UnitsType
    ) : ForecastItemViewHolder(bind.root, unitsType) {
        override fun bind(item: IForecastItem) {
            if (item !is IForecastItem.IHourlyPressure) return
            updateValue(
                item.pressureMb,
                item.pressureIn,
                unitsType
            )
            updateTime(item.date)
        }

        private fun updateValue(
            pressureMb: Double,
            pressureIn: Double,
            unitsType: UnitsType
        ) = with(bind) {

            val (strRes, pressure) = when(unitsType) {
                UnitsType.METRIC -> Pair(
                    CoreUiRes.string.template_mmhg,
                    pressureMb.roundToInt()
                )
                UnitsType.IMPERIAL -> Pair(
                    CoreUiRes.string.template_mb,
                    pressureIn.roundToInt()
                )
            }

            this.value.text = itemView.context
                .getString(strRes, pressure)
        }

        private fun updateTime(time: Date) = with(bind) {
            this.time.text = mTimeFormatter.format(time)
        }
    }
}
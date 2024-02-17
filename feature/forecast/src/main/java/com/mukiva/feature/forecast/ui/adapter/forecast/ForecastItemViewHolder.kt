package com.mukiva.feature.forecast.ui.adapter.forecast

import android.graphics.drawable.RotateDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mukiva.feature.forecast.R
import com.mukiva.feature.forecast.databinding.ItemHumidityBinding
import com.mukiva.feature.forecast.databinding.ItemPressureBinding
import com.mukiva.feature.forecast.databinding.ItemTempBinding
import com.mukiva.feature.forecast.databinding.ItemWindBinding
import com.mukiva.feature.forecast.domain.IForecastItem
import com.mukiva.feature.forecast.domain.WindDirection
import java.util.Date
import java.text.SimpleDateFormat

sealed class ForecastItemViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    protected val mTimeFormatter = SimpleDateFormat(
        "HH:mm",
        itemView.context.resources.configuration.locales[0]
    )

    abstract fun bind(item: IForecastItem)

    class TempItemViewHolder(
        private val bind: ItemTempBinding
    ) : ForecastItemViewHolder(bind.root) {
        override fun bind(item: IForecastItem) {
            if (item !is IForecastItem.IHourlyTemp) return
            updateTemp(item.tempC, item.tempF)
            updateFeelsLike(item.feelsLikeC, item.feelsLikeF)
            updateCloud(item.cloud)
            updateCloudIcon(item.iconUrl)
            updateTime(item.date)
        }

        private fun updateTemp(tempC: Double, tempF: Double) = with(bind) {
            tempValue.text = "${tempC.toInt()}"
        }

        private fun updateFeelsLike(tempC: Double, tempF: Double) = with(bind) {
            feelsLikeValue.text = "${tempC.toInt()}"
        }

        private fun updateCloud(cloud: Double) = with(bind) {
            cloudValue.text = "${cloud.toInt()}"
        }

        private fun updateCloudIcon(iconUrl: String) = with(bind) {
            Glide.with(root)
                .load("https:$iconUrl")
                .into(icon)
        }

        private fun updateTime(date: Date) = with(bind) {
            time.text = mTimeFormatter.format(date)
        }
    }

    class WindItemViewHolder(
        private val bind: ItemWindBinding
    ) : ForecastItemViewHolder(bind.root) {

        override fun bind(item: IForecastItem) {
            if (item !is IForecastItem.IHourlyWind) return

            updateWindDegree(item.windDegree)
            updateWindSpeed(item.windMph, item.windKph)
            updateTime(item.date)
            updateIcon(item.windDegree, item.windDirection)

        }

        private fun updateWindSpeed(speedMph: Double, speedKph: Double) = with(bind) {
            windSpeedValue.text = "${speedKph.toInt()}"
        }

        private fun updateWindDegree(degree: Int) = with(bind) {
            degreeValue.text = "$degree"
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
        private val bind: ItemHumidityBinding
    ) : ForecastItemViewHolder(bind.root) {
        override fun bind(item: IForecastItem) {
            if (item !is IForecastItem.IHourlyHumidity) return
            updateValue(item.humidity)
            updateTime(item.date)
        }

        private fun updateValue(value: Int) = with(bind) {
            this.value.text = "$value"
        }

        private fun updateTime(time: Date) = with(bind) {
            this.time.text = mTimeFormatter.format(time)
        }
    }

    class PressureItemViewHolder(
        private val bind: ItemPressureBinding
    ) : ForecastItemViewHolder(bind.root) {
        override fun bind(item: IForecastItem) {
            if (item !is IForecastItem.IHourlyPressure) return
            updateValue(item.pressureMb, item.pressureIn)
            updateTime(item.date)
        }

        private fun updateValue(
            pressureMb: Double,
            pressureIn: Double
        ) = with(bind) {
            this.value.text = "${pressureMb.toInt()}"
        }

        private fun updateTime(time: Date) = with(bind) {
            this.time.text = mTimeFormatter.format(time)
        }
    }
}
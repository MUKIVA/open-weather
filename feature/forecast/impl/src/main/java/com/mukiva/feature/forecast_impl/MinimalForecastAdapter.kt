package com.mukiva.feature.forecast_impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mukiva.feature.forecast_impl.databinding.ItemMinForecastBinding
import com.mukiva.feature.forecast_impl.domain.Forecast


class MinimalForecastItemViewHolder(
    private val bindings: ItemMinForecastBinding,
    private val onItemClick: () -> Unit
) : RecyclerView.ViewHolder(bindings.root) {

    fun bind(item: Forecast) = with(bindings) {
        root.setOnClickListener {
            onItemClick()
        }

    }

}

object MinimalForecastItemDiffCallback : DiffUtil.ItemCallback<Forecast>() {
    override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
        return oldItem == newItem
    }

}

class MinimalForecastAdapter(
    private val onItemClick: () -> Unit
) : ListAdapter<Forecast, MinimalForecastItemViewHolder>(MinimalForecastItemDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MinimalForecastItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MinimalForecastItemViewHolder(
            bindings = ItemMinForecastBinding.inflate(inflater, parent, false),
            onItemClick = onItemClick
        )
    }

    override fun onBindViewHolder(holder: MinimalForecastItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
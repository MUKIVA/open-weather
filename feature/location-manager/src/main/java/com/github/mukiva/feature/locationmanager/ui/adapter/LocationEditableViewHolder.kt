package com.github.mukiva.feature.locationmanager.ui.adapter

import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.github.mukiva.feature.locationmanager.R
import com.github.mukiva.feature.locationmanager.databinding.ItemLocationEditableBinding
import com.github.mukiva.feature.locationmanager.domain.model.Location
import com.github.mukiva.feature.locationmanager.presentation.EditableLocation

internal class LocationEditableViewHolder(
    private val bind: ItemLocationEditableBinding,
    private val onEdit: (EditableLocation) -> Unit,
    private val onSelect: (EditableLocation) -> Unit
) : RecyclerView.ViewHolder(bind.root) {

    fun bind(item: EditableLocation) {
        setLocationData(item.location)
        setSelected(item.isSelected)
        setEditable(item.isEditable)

        setActions(item)
    }

    fun bindWithPayloads(
        item: EditableLocation,
        payloads: List<EditableLocationPayload>
    ) = with(bind) {
        payloads.onEach { payload ->
            when (payload) {
                EditableLocationPayload.CITY_NAME ->
                    cityName.text = item.location.cityName
                EditableLocationPayload.REGION_NAME ->
                    region.text = item.location.regionName
                EditableLocationPayload.COUNTRY_NAME ->
                    country.text = item.location.countryName
                EditableLocationPayload.IS_EDITABLE ->
                    setEditable(item.isEditable)
                EditableLocationPayload.IS_SELECTED ->
                    setSelected(item.isSelected)
            }
        }

        setActions(item)
    }

    private fun setActions(item: EditableLocation) {
        setLongTap(item)
        setClick(item)
    }

    private fun setClick(item: EditableLocation) = with(bind) {
        if (item.isEditable) {
            root.setOnClickListener { onSelect(item) }
        } else {
            root.setOnClickListener(null)
        }
    }

    private fun setLongTap(item: EditableLocation) = with(bind) {
        if (item.isEditable) {
            root.setOnLongClickListener(null)
        } else {
            root.setOnLongClickListener {
                onEdit(item)
                true
            }
        }
    }

    private fun setLocationData(item: Location) = with(bind) {
        cityName.text = item.cityName
        region.text = item.regionName
        country.text = item.countryName
    }

    private fun setEditable(isEditable: Boolean) = with(bind) {
        dragHandleIcon.isVisible = isEditable
    }

    private fun setSelected(isSelected: Boolean) = with(bind) {
        val res = when (isSelected) {
            true -> R.drawable.background_editable_item_selected
            false -> R.drawable.background_editable_item
        }
        container.background = AppCompatResources.getDrawable(itemView.context, res)
    }
}

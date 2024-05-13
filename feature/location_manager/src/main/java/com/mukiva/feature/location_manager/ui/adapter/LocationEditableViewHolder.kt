package com.mukiva.feature.location_manager.ui.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mukiva.feature.location_manager.databinding.ItemLocationEditableBinding
import com.mukiva.feature.location_manager.domain.model.Location
import com.mukiva.feature.location_manager.presentation.EditableLocation

class LocationEditableViewHolder(
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
            root.setOnLongClickListener { onEdit(item); true }
        }
    }

    private fun setLocationData(item: Location) = with(bind) {
        cityName.text = item.cityName
        region.text = item.regionName
        country.text = item.countryName
    }

    private fun setEditable(isEditable: Boolean) = with(bind) {
        selectCheckBox.isVisible = isEditable
        dragHandleIcon.isVisible = isEditable
    }

    private fun setSelected(isSelected: Boolean) = with(bind) {
        selectCheckBox.isChecked = isSelected
        selectCheckBox.isEnabled = false
    }


}
package com.mukiva.feature.location_manager_impl.ui.adapter

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.ofInt
import android.app.ActionBar.LayoutParams
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.mukiva.feature.location_manager_impl.databinding.ItemLocationEditableBinding
import com.mukiva.feature.location_manager_impl.domain.model.Location
import com.mukiva.feature.location_manager_impl.presentation.EditableLocation
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.visible

class LocationEditableViewHolder(
    private val bind: ItemLocationEditableBinding,
    private val onEdit: (EditableLocation) -> Unit,
    private val onSelect: (EditableLocation) -> Unit
) : RecyclerView.ViewHolder(bind.root) {

    fun bind(item: EditableLocation) = with(bind) {
        setLocationData(item.location)
        setSelected(item.isSelected)
        setEditable(item.isEditable)

        setActions(item)
    }

    private fun setActions(item: EditableLocation) = with(bind) {
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
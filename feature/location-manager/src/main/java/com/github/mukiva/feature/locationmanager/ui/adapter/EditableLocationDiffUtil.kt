package com.github.mukiva.feature.locationmanager.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.github.mukiva.feature.locationmanager.presentation.EditableLocation

internal object EditableLocationDiffUtil : DiffUtil.ItemCallback<EditableLocation>() {
    override fun areItemsTheSame(oldItem: EditableLocation, newItem: EditableLocation): Boolean {
        return oldItem.location.id == newItem.location.id
    }

    override fun areContentsTheSame(oldItem: EditableLocation, newItem: EditableLocation): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(
        oldItem: EditableLocation,
        newItem: EditableLocation
    ): MutableList<EditableLocationPayload> {
        val diffList = ArrayList<EditableLocationPayload>(EditableLocationPayload.entries.size)
        if (oldItem.location.cityName != newItem.location.cityName) {
            diffList.add(EditableLocationPayload.CITY_NAME)
        }
        if (oldItem.location.regionName != newItem.location.regionName) {
            diffList.add(EditableLocationPayload.REGION_NAME)
        }
        if (oldItem.location.countryName != newItem.location.countryName) {
            diffList.add(EditableLocationPayload.COUNTRY_NAME)
        }
        if (oldItem.isEditable != newItem.isEditable) {
            diffList.add(EditableLocationPayload.IS_EDITABLE)
        }
        if (oldItem.isSelected != newItem.isSelected) {
            diffList.add(EditableLocationPayload.IS_SELECTED)
        }
        return diffList
    }
}

enum class EditableLocationPayload {
    CITY_NAME,
    REGION_NAME,
    COUNTRY_NAME,
    IS_EDITABLE,
    IS_SELECTED,
}

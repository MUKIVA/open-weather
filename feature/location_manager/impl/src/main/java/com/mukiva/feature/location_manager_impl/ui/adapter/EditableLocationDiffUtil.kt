package com.mukiva.feature.location_manager_impl.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.location_manager_impl.presentation.EditableLocation

object EditableLocationDiffUtil : DiffUtil.ItemCallback<EditableLocation>() {
    override fun areItemsTheSame(oldItem: EditableLocation, newItem: EditableLocation): Boolean {
        return oldItem.location.uid == newItem.location.uid
    }

    override fun areContentsTheSame(oldItem: EditableLocation, newItem: EditableLocation): Boolean {
        return oldItem == newItem
    }
}
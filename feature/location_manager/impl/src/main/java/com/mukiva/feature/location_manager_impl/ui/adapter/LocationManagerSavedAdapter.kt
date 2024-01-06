package com.mukiva.feature.location_manager_impl.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mukiva.feature.location_manager_impl.databinding.ItemLocationEditableBinding
import com.mukiva.feature.location_manager_impl.presentation.EditableLocation

class LocationManagerSavedAdapter(
    private val onEnterEditMode: (EditableLocation) -> Unit,
    private val onSelectEditable: (EditableLocation) -> Unit,
    private val onItemRemove: (EditableLocation) -> Unit,
    private val onItemMoveCallback: (Int, Int) -> Unit
) : ListAdapter<EditableLocation, LocationEditableViewHolder>(EditableLocationDiffUtil), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationEditableViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LocationEditableViewHolder(
            bind = ItemLocationEditableBinding.inflate(inflater, parent, false),
            onEdit = onEnterEditMode,
            onSelect = onSelectEditable
        )
    }

    override fun onBindViewHolder(holder: LocationEditableViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        onItemMoveCallback(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        onItemRemove(getItem(position))
    }

}
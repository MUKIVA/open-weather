package com.github.mukiva.feature.locationmanager.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.mukiva.feature.locationmanager.databinding.ItemLocationEditableBinding
import com.github.mukiva.feature.locationmanager.presentation.EditableLocation

@Suppress("UNCHECKED_CAST")
class LocationManagerSavedAdapter(
    private val onEnterEditMode: (EditableLocation) -> Unit,
    private val onSelectEditable: (EditableLocation) -> Unit,
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

    override fun onBindViewHolder(
        holder: LocationEditableViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            holder.bindWithPayloads(getItem(position), payloads[0] as List<EditableLocationPayload>)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        onItemMoveCallback(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}

package com.mukiva.feature.location_manager.presentation

import com.mukiva.feature.location_manager.domain.model.Location

data class EditableLocation(
    val location: Location,
    val isSelected: Boolean,
    val isEditable: Boolean
) {
    companion object {
        val ListState<EditableLocation>.selectedCount get() = this.list.count { it.isSelected }

    }
}
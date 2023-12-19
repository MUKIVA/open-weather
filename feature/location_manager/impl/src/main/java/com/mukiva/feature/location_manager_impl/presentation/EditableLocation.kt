package com.mukiva.feature.location_manager_impl.presentation

import com.mukiva.feature.location_manager_impl.domain.model.Location

data class EditableLocation(
    val location: Location,
    val isSelected: Boolean,
    val isEditable: Boolean
)
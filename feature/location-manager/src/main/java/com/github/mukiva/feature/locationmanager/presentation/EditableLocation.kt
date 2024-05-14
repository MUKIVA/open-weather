package com.github.mukiva.feature.locationmanager.presentation

import com.github.mukiva.feature.locationmanager.domain.model.Location

data class EditableLocation(
    val location: Location,
    val isSelected: Boolean,
    val isEditable: Boolean
)

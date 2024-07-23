package com.github.mukiva.feature.locationmanager.presentation

import com.github.mukiva.feature.locationmanager.domain.model.Location

internal data class EditableLocation(
    val location: Location,
    val isSelected: Boolean,
    val isEditable: Boolean
)

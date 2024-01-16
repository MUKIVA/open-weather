package com.mukiva.feature.location_manager.presentation

sealed class LocationManagerEvent {
    data class Toast(val msg: String) : LocationManagerEvent()
}
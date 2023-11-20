package com.mukiva.feature.location_manager_impl.presentation

sealed class LocationManagerEvent {
    data class Toast(val msg: String) : LocationManagerEvent()
}
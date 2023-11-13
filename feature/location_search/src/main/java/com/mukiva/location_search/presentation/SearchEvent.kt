package com.mukiva.location_search.presentation

sealed class SearchEvent {
    data class Toast(val msg: String) : SearchEvent()
}
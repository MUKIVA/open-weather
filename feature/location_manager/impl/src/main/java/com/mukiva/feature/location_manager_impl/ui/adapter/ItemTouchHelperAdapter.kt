package com.mukiva.feature.location_manager_impl.ui.adapter

interface ItemTouchHelperAdapter  {
    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)
}
package com.mukiva.core.data.repository

interface IForecastUpdater {

    val isTimeForUpdate: Boolean
    fun markForUpdate()
    fun commitUpdate()
    fun observeUpdate(block: () -> Unit)
}
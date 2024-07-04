package com.github.mukiva.weatherdata.editors

import kotlinx.coroutines.flow.Flow

interface ILaunchGroupEditor {
    suspend fun setFirstOpenComplete(isComplete: Boolean)
    fun getFirstOpenComplete(): Flow<Boolean>
}
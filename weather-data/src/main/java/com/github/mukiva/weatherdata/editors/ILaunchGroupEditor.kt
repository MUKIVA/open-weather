package com.github.mukiva.weatherdata.editors

import kotlinx.coroutines.flow.Flow

public interface ILaunchGroupEditor {
    public suspend fun setFirstOpenComplete(isComplete: Boolean)
    public fun getFirstOpenComplete(): Flow<Boolean>
}
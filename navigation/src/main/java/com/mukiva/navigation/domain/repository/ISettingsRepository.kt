package com.mukiva.navigation.domain.repository

import com.mukiva.navigation.domain.Theme
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    fun getThemeFlow(): Flow<Theme>

}
package com.yumedev.seijakulistkmp.features.settings.domain.repository

import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getThemeMode(): Flow<ThemeMode>
    suspend fun setThemeMode(themeMode: ThemeMode)
}

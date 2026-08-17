package com.yumedev.seijakulistkmp.features.settings.data.repository

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val settings: ObservableSettings
) : SettingsRepository {

    private val flowSettings = settings.toFlowSettings()

    override fun getThemeMode(): Flow<ThemeMode> {
        return flowSettings.getStringFlow(KEY_THEME_MODE, ThemeMode.SYSTEM.name)
            .map { themeName ->
                try {
                    ThemeMode.valueOf(themeName)
                } catch (e: IllegalArgumentException) {
                    ThemeMode.SYSTEM
                }
            }
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        settings.putString(KEY_THEME_MODE, themeMode.name)
    }

    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
    }
}

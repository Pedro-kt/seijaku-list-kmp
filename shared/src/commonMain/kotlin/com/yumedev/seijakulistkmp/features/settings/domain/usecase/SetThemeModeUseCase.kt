package com.yumedev.seijakulistkmp.features.settings.domain.usecase

import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository

class SetThemeModeUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(themeMode: ThemeMode) {
        settingsRepository.setThemeMode(themeMode)
    }
}

package com.yumedev.seijakulistkmp.features.settings.domain.usecase

import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetThemeModeUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<ThemeMode> {
        return settingsRepository.getThemeMode()
    }
}

package com.yumedev.seijakulistkmp.features.settings.domain.usecase

import com.yumedev.seijakulistkmp.features.settings.domain.model.LanguageMode
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository

class SetLanguageModeUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(languageMode: LanguageMode) {
        settingsRepository.setLanguageMode(languageMode)
    }
}

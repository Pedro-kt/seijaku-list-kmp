package com.yumedev.seijakulistkmp.features.settings.domain.usecase

import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeVariant
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository

class SetThemeVariantUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(themeVariant: ThemeVariant) {
        settingsRepository.setThemeVariant(themeVariant)
    }
}

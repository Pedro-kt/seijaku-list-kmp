package com.yumedev.seijakulistkmp.features.settings.domain.usecase

import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeVariant
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetThemeVariantUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<ThemeVariant> {
        return settingsRepository.getThemeVariant()
    }
}

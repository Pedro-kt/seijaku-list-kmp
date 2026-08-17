package com.yumedev.seijakulistkmp.features.settings.domain.usecase

import com.yumedev.seijakulistkmp.features.settings.domain.model.LanguageMode
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetLanguageModeUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<LanguageMode> {
        return settingsRepository.getLanguageMode()
    }
}

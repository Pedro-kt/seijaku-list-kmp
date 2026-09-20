package com.yumedev.seijakulistkmp.features.settings.domain.usecase

import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository

class SetAiringNotificationsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        settingsRepository.setAiringNotifications(enabled)
    }
}

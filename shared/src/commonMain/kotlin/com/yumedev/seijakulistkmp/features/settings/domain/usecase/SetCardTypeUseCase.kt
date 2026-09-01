package com.yumedev.seijakulistkmp.features.settings.domain.usecase

import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import com.yumedev.seijakulistkmp.features.tracking.presentation.components.MediaListCardType

class SetCardTypeUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(cardType: MediaListCardType) {
        settingsRepository.setCardType(cardType)
    }
}

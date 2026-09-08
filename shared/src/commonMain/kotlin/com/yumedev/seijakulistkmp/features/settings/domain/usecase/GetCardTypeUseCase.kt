package com.yumedev.seijakulistkmp.features.settings.domain.usecase

import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import com.yumedev.seijakulistkmp.features.tracking.presentation.components.MediaListCardType
import kotlinx.coroutines.flow.Flow

class GetCardTypeUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<MediaListCardType> {
        return settingsRepository.getCardType()
    }
}

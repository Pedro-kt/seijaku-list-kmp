package com.yumedev.seijakulistkmp.features.settings.domain.repository

import com.yumedev.seijakulistkmp.features.settings.domain.model.LanguageMode
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import com.yumedev.seijakulistkmp.features.tracking.presentation.components.MediaListCardType
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getThemeMode(): Flow<ThemeMode>
    suspend fun setThemeMode(themeMode: ThemeMode)

    fun getLanguageMode(): Flow<LanguageMode>
    suspend fun setLanguageMode(languageMode: LanguageMode)

    fun getSfwMode(): Flow<Boolean>
    suspend fun setSfwMode(enabled: Boolean)

    fun getCardType(): Flow<MediaListCardType>
    suspend fun setCardType(cardType: MediaListCardType)
}

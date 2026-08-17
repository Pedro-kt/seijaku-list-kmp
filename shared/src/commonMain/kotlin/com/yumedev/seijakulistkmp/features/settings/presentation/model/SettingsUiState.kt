package com.yumedev.seijakulistkmp.features.settings.presentation.model

import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode

data class SettingsUiState(
    val selectedTheme: ThemeMode = ThemeMode.SYSTEM,
    val airingNotificationsEnabled: Boolean = false,
    val sfwModeEnabled: Boolean = false,
    val lastSyncTime: String? = null,
    val cacheSize: String = "0 MB",
    val username: String = "",
    val userHandle: String = "",
    val appVersion: String = "1.0",
    val buildNumber: String = "104"
)

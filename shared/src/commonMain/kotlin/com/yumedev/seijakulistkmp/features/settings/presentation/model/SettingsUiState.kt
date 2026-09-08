package com.yumedev.seijakulistkmp.features.settings.presentation.model

import com.yumedev.seijakulistkmp.features.settings.domain.model.LanguageMode
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ImportConflict
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ImportResult

data class SettingsUiState(
    val selectedTheme: ThemeMode = ThemeMode.SYSTEM,
    val selectedLanguage: LanguageMode = LanguageMode.SYSTEM,
    val airingNotificationsEnabled: Boolean = false,
    val sfwModeEnabled: Boolean = true,
    val lastSyncTime: String? = null,
    val cacheSize: String = "0 MB",
    val username: String = "",
    val userHandle: String = "",
    val isImporting: Boolean = false,
    val importResult: ImportResult? = null,
    val showImportResultDialog: Boolean = false,
    val showConflictDialog: Boolean = false,
    val currentConflicts: List<ImportConflict> = emptyList()
)

package com.yumedev.seijakulistkmp.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.features.settings.domain.model.LanguageMode
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetLanguageModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetThemeModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetLanguageModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetThemeModeUseCase
import com.yumedev.seijakulistkmp.features.settings.presentation.model.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val getLanguageModeUseCase: GetLanguageModeUseCase,
    private val setLanguageModeUseCase: SetLanguageModeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        observeThemeMode()
        observeLanguageMode()
    }

    private fun observeThemeMode() {
        viewModelScope.launch {
            getThemeModeUseCase().collect { themeMode ->
                _state.update { it.copy(selectedTheme = themeMode) }
            }
        }
    }

    private fun observeLanguageMode() {
        viewModelScope.launch {
            getLanguageModeUseCase().collect { languageMode ->
                _state.update { it.copy(selectedLanguage = languageMode) }
            }
        }
    }

    fun onThemeSelected(themeMode: ThemeMode) {
        viewModelScope.launch {
            setThemeModeUseCase(themeMode)
        }
    }

    fun onLanguageSelected(languageMode: LanguageMode) {
        viewModelScope.launch {
            setLanguageModeUseCase(languageMode)
        }
    }

    fun onAiringNotificationsToggle(enabled: Boolean) {
        _state.update { it.copy(airingNotificationsEnabled = enabled) }
        // TODO: Implement persistence for airing notifications
    }

    fun onSfwModeToggle(enabled: Boolean) {
        _state.update { it.copy(sfwModeEnabled = enabled) }
        // TODO: Implement persistence for SFW mode
    }

    fun onSyncClick() {
        // TODO: Implement AniList sync
    }

    fun onDownloadListClick() {
        // TODO: Implement list download
    }

    fun onClearCacheClick() {
        // TODO: Implement cache clearing
    }

    fun onAboutClick() {
        // TODO: Navigate to about screen
    }

    fun onLogoutClick() {
        // TODO: Implement logout
    }
}

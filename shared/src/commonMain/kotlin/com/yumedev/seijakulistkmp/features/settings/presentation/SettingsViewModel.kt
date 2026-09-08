package com.yumedev.seijakulistkmp.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.settings.domain.model.LanguageMode
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetLanguageModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetSfwModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetThemeModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetLanguageModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetSfwModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetThemeModeUseCase
import com.yumedev.seijakulistkmp.features.settings.presentation.model.SettingsUiState
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ConflictResolution
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ImportConflict
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ExportToMALUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ImportFromMALUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ResolveAllImportConflictsUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ResolveImportConflictUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val getLanguageModeUseCase: GetLanguageModeUseCase,
    private val setLanguageModeUseCase: SetLanguageModeUseCase,
    private val getSfwModeUseCase: GetSfwModeUseCase,
    private val setSfwModeUseCase: SetSfwModeUseCase,
    private val exportToMALUseCase: ExportToMALUseCase,
    private val importFromMALUseCase: ImportFromMALUseCase,
    private val resolveImportConflictUseCase: ResolveImportConflictUseCase,
    private val resolveAllImportConflictsUseCase: ResolveAllImportConflictsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        observeThemeMode()
        observeLanguageMode()
        observeSfwMode()
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

    private fun observeSfwMode() {
        viewModelScope.launch {
            getSfwModeUseCase().collect { sfwEnabled ->
                _state.update { it.copy(sfwModeEnabled = sfwEnabled) }
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
        viewModelScope.launch {
            setSfwModeUseCase(enabled)
        }
    }

    fun onSyncClick() {
        // TODO: Implement AniList sync
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

    fun onExportAnimeClick(onExport: (String, String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            exportToMALUseCase(MediaType.ANIME)
                .onSuccess { xmlContent ->
                    onExport(xmlContent, "animelist.xml")
                }
                .onFailure { exception ->
                    onError(exception.message ?: "Error exporting anime list")
                }
        }
    }

    fun onExportMangaClick(onExport: (String, String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            exportToMALUseCase(MediaType.MANGA)
                .onSuccess { xmlContent ->
                    onExport(xmlContent, "mangalist.xml")
                }
                .onFailure { exception ->
                    onError(exception.message ?: "Error exporting manga list")
                }
        }
    }

    fun onImportAnimeClick(xmlContent: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isImporting = true) }

            importFromMALUseCase(xmlContent, MediaType.ANIME)
                .onSuccess { importResult ->
                    _state.update {
                        it.copy(
                            isImporting = false,
                            importResult = importResult,
                            showImportResultDialog = true,
                            currentConflicts = importResult.conflicts
                        )
                    }
                    onSuccess()
                }
                .onFailure { exception ->
                    exception.printStackTrace()
                    _state.update { it.copy(isImporting = false) }
                    onError(exception.message ?: "Error importing anime list")
                }
        }
    }

    fun onImportMangaClick(xmlContent: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isImporting = true) }

            importFromMALUseCase(xmlContent, MediaType.MANGA)
                .onSuccess { importResult ->
                    _state.update {
                        it.copy(
                            isImporting = false,
                            importResult = importResult,
                            showImportResultDialog = true,
                            currentConflicts = importResult.conflicts
                        )
                    }
                    onSuccess()
                }
                .onFailure { exception ->
                    exception.printStackTrace()
                    _state.update { it.copy(isImporting = false) }
                    onError(exception.message ?: "Error importing manga list")
                }
        }
    }

    fun dismissImportResultDialog() {
        _state.update {
            it.copy(
                showImportResultDialog = false,
                importResult = null
            )
        }
    }

    fun showConflictResolutionDialog() {
        _state.update {
            it.copy(
                showImportResultDialog = false,
                showConflictDialog = true
            )
        }
    }

    fun dismissConflictDialog() {
        _state.update {
            it.copy(
                showConflictDialog = false,
                currentConflicts = emptyList()
            )
        }
    }

    fun resolveConflict(conflict: ImportConflict, resolution: ConflictResolution) {
        viewModelScope.launch {
            resolveImportConflictUseCase(conflict, resolution)
                .onSuccess {
                    _state.update {
                        it.copy(
                            currentConflicts = it.currentConflicts.filter { c -> c != conflict }
                        )
                    }

                    if (_state.value.currentConflicts.isEmpty()) {
                        dismissConflictDialog()
                    }
                }
                .onFailure {
                    // Handle error
                }
        }
    }

    fun resolveAllConflicts(resolution: ConflictResolution) {
        viewModelScope.launch {
            resolveAllImportConflictsUseCase(_state.value.currentConflicts, resolution)
                .onSuccess {
                    _state.update {
                        it.copy(
                            currentConflicts = emptyList(),
                            showConflictDialog = false
                        )
                    }
                }
                .onFailure {
                    // Handle error
                }
        }
    }
}

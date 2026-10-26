package com.yumedev.seijakulistkmp.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.analytics.AnalyticsEvents
import com.yumedev.seijakulistkmp.core.analytics.AnalyticsManager
import com.yumedev.seijakulistkmp.core.analytics.AnalyticsParams
import com.yumedev.seijakulistkmp.core.analytics.AnalyticsUserProperties
import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.notification.AiringNotificationScheduler
import com.yumedev.seijakulistkmp.features.auth.domain.usecase.GetCurrentUserUseCase
import com.yumedev.seijakulistkmp.features.auth.domain.usecase.LogoutUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.SyncFavoriteEpisodesWithFirestoreUseCase
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.GetCurrentProfileUseCase
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.SyncProfileWithFirestoreUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.model.LanguageMode
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeVariant
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetAiringNotificationsUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetLanguageModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetSfwModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetThemeModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetThemeVariantUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetAiringNotificationsUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetLanguageModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetSfwModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetThemeModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetThemeVariantUseCase
import com.yumedev.seijakulistkmp.features.settings.presentation.model.SettingsUiState
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ConflictResolution
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ImportConflict
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ExportToMALUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ImportFromMALUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ResolveAllImportConflictsUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ResolveImportConflictUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.SyncMediaListWithFirestoreUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val getThemeVariantUseCase: GetThemeVariantUseCase,
    private val setThemeVariantUseCase: SetThemeVariantUseCase,
    private val getLanguageModeUseCase: GetLanguageModeUseCase,
    private val setLanguageModeUseCase: SetLanguageModeUseCase,
    private val getSfwModeUseCase: GetSfwModeUseCase,
    private val setSfwModeUseCase: SetSfwModeUseCase,
    private val getAiringNotificationsUseCase: GetAiringNotificationsUseCase,
    private val setAiringNotificationsUseCase: SetAiringNotificationsUseCase,
    private val airingNotificationScheduler: AiringNotificationScheduler,
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val exportToMALUseCase: ExportToMALUseCase,
    private val importFromMALUseCase: ImportFromMALUseCase,
    private val resolveImportConflictUseCase: ResolveImportConflictUseCase,
    private val resolveAllImportConflictsUseCase: ResolveAllImportConflictsUseCase,
    private val syncProfileWithFirestoreUseCase: SyncProfileWithFirestoreUseCase,
    private val syncMediaListWithFirestoreUseCase: SyncMediaListWithFirestoreUseCase,
    private val syncFavoriteEpisodesWithFirestoreUseCase: SyncFavoriteEpisodesWithFirestoreUseCase,
    private val analyticsManager: AnalyticsManager,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        observeThemeMode()
        observeThemeVariant()
        observeLanguageMode()
        observeSfwMode()
        observeAiringNotifications()
        observeCurrentProfile()
        observeAuthUser()
    }

    private fun observeThemeMode() {
        viewModelScope.launch {
            getThemeModeUseCase().collect { themeMode ->
                _state.update { it.copy(selectedTheme = themeMode) }
            }
        }
    }

    private fun observeThemeVariant() {
        viewModelScope.launch {
            getThemeVariantUseCase().collect { themeVariant ->
                _state.update { it.copy(selectedThemeVariant = themeVariant) }
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

    private fun observeAiringNotifications() {
        viewModelScope.launch {
            getAiringNotificationsUseCase().collect { enabled ->
                _state.update { it.copy(airingNotificationsEnabled = enabled) }
            }
        }
    }

    private fun observeCurrentProfile() {
        viewModelScope.launch {
            getCurrentProfileUseCase().collect { profile ->
                _state.update { currentState ->
                    currentState.copy(
                        username = profile?.name ?: currentState.username
                    )
                }
            }
        }
    }

    private fun observeAuthUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { authUser ->
                _state.update { currentState ->
                    if (authUser != null) {
                        currentState.copy(
                            userHandle = authUser.email ?: authUser.displayName ?: "User",
                            isLoggedIn = !authUser.isAnonymous
                        )
                    } else {
                        currentState.copy(
                            userHandle = "Local",
                            isLoggedIn = false
                        )
                    }
                }
            }
        }
    }

    fun onThemeSelected(themeMode: ThemeMode) {
        viewModelScope.launch {
            setThemeModeUseCase(themeMode)
            analyticsManager.logEvent(AnalyticsEvents.THEME_CHANGED, mapOf(
                AnalyticsParams.THEME to themeMode.name
            ))
            analyticsManager.setUserProperty(AnalyticsUserProperties.THEME_MODE, themeMode.name)
        }
    }

    fun onThemeVariantSelected(themeVariant: ThemeVariant) {
        viewModelScope.launch {
            setThemeVariantUseCase(themeVariant)
        }
    }

    fun onLanguageSelected(languageMode: LanguageMode) {
        viewModelScope.launch {
            setLanguageModeUseCase(languageMode)
            analyticsManager.logEvent(AnalyticsEvents.LANGUAGE_CHANGED, mapOf(
                AnalyticsParams.LANGUAGE to languageMode.name
            ))
            analyticsManager.setUserProperty(AnalyticsUserProperties.LANGUAGE, languageMode.name)
        }
    }

    fun onAiringNotificationsToggle(enabled: Boolean) {
        viewModelScope.launch {
            setAiringNotificationsUseCase(enabled)

            if (enabled) {
                airingNotificationScheduler.startPeriodicSync()
                analyticsManager.logEvent(AnalyticsEvents.ENABLE_NOTIFICATIONS)
            } else {
                airingNotificationScheduler.cancelAll()
                analyticsManager.logEvent(AnalyticsEvents.DISABLE_NOTIFICATIONS)
            }

            analyticsManager.setUserProperty(
                AnalyticsUserProperties.NOTIFICATIONS_ENABLED,
                enabled.toString()
            )
        }
    }

    fun onSfwModeToggle(enabled: Boolean) {
        viewModelScope.launch {
            setSfwModeUseCase(enabled)
        }
    }

    fun onSyncClick() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase().first()
            if (user == null) {
                return@launch
            }

            _state.update { it.copy(isSyncing = true) }

            try {
                syncProfileWithFirestoreUseCase()
                syncMediaListWithFirestoreUseCase()
                syncFavoriteEpisodesWithFirestoreUseCase()

                _state.update { it.copy(
                    isSyncing = false,
                    syncMessage = "Sync completed successfully"
                )}
            } catch (e: Exception) {
                _state.update { it.copy(
                    isSyncing = false,
                    syncMessage = "Sync failed: ${e.message}"
                )}
            }
        }
    }

    fun onClearCacheClick() {
        _state.update { it.copy(clearCacheMessage = "Cache clearing is not yet implemented. Use app settings to clear app data.") }
    }

    fun dismissSyncMessage() {
        _state.update { it.copy(syncMessage = null) }
    }

    fun dismissClearCacheMessage() {
        _state.update { it.copy(clearCacheMessage = null) }
    }

    fun onAboutClick() {
        _state.update { it.copy(pendingExternalUrl = "https://daffdev.vercel.app/privacy") }
    }

    fun onAniListClick() {
        _state.update { it.copy(pendingExternalUrl = "https://anilist.co/") }
    }

    fun dismissExternalLinkDialog() {
        _state.update { it.copy(pendingExternalUrl = null) }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            analyticsManager.logEvent(AnalyticsEvents.LOGOUT)
            analyticsManager.setUserId(null)
            logoutUseCase()
        }
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

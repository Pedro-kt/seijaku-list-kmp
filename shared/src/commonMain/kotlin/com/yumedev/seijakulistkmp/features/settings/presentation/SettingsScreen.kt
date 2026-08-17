package com.yumedev.seijakulistkmp.features.settings.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yumedev.seijakulistkmp.features.settings.domain.model.LanguageMode
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import com.yumedev.seijakulistkmp.features.settings.presentation.components.*
import com.yumedev.seijakulistkmp.features.settings.presentation.model.SettingsUiState
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowLeft
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import seijakulistkmp.shared.generated.resources.*

class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<SettingsViewModel>()
        val state by viewModel.state.collectAsState()

        SettingsScreenContent(
            onBackClick = { navigator.pop() },
            state = state,
            onThemeSelected = viewModel::onThemeSelected,
            onLanguageSelected = viewModel::onLanguageSelected,
            onAiringNotificationsToggle = viewModel::onAiringNotificationsToggle,
            onSfwModeToggle = viewModel::onSfwModeToggle,
            onSyncClick = viewModel::onSyncClick,
            onDownloadListClick = viewModel::onDownloadListClick,
            onClearCacheClick = viewModel::onClearCacheClick,
            onAboutClick = viewModel::onAboutClick,
            onLogoutClick = viewModel::onLogoutClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    onBackClick: () -> Unit,
    state: SettingsUiState,
    onThemeSelected: (ThemeMode) -> Unit,
    onLanguageSelected: (LanguageMode) -> Unit,
    onAiringNotificationsToggle: (Boolean) -> Unit,
    onSfwModeToggle: (Boolean) -> Unit,
    onSyncClick: () -> Unit,
    onDownloadListClick: () -> Unit,
    onClearCacheClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.settings),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = TablerIcons.Outlined.ArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                SettingsAppearanceSection(
                    selectedTheme = state.selectedTheme,
                    selectedLanguage = state.selectedLanguage,
                    onThemeSelected = onThemeSelected,
                    onLanguageSelected = onLanguageSelected
                )
            }

            item {
                SettingsContentSection(
                    airingNotificationsEnabled = state.airingNotificationsEnabled,
                    sfwModeEnabled = state.sfwModeEnabled,
                    onAiringNotificationsToggle = onAiringNotificationsToggle,
                    onSfwModeToggle = onSfwModeToggle
                )
            }

            item {
                SettingsDataSection(
                    lastSyncTime = state.lastSyncTime,
                    cacheSize = state.cacheSize,
                    onSyncClick = onSyncClick,
                    onDownloadListClick = onDownloadListClick,
                    onClearCacheClick = onClearCacheClick,
                    onAboutClick = onAboutClick
                )
            }

            item {
                SettingsAccountSection(
                    username = state.username,
                    userHandle = state.userHandle,
                    appVersion = state.appVersion,
                    buildNumber = state.buildNumber,
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}

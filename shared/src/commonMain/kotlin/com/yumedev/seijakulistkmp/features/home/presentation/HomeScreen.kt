package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Bell
import dev.seyfarth.tablericons.outlined.Search
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.app_name
import seijakulistkmp.shared.generated.resources.notifications
import seijakulistkmp.shared.generated.resources.search

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeScreenContent()
    }
}

@Composable
fun HomeScreenContent() {
    Scaffold(
        topBar = {
            HomeTopAppBar(
                onSearchClick = {
                    // TODO: Navigate to search or open search dialog
                },
                onNotificationsClick = {
                    // TODO: Navigate to notifications screen
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Home Content",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = TablerIcons.Outlined.Search,
                    contentDescription = stringResource(Res.string.search)
                )
            }
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = TablerIcons.Outlined.Bell,
                    contentDescription = stringResource(Res.string.notifications)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

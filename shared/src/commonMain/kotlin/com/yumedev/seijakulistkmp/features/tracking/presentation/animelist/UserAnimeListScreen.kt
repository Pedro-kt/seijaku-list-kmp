package com.yumedev.seijakulistkmp.features.tracking.presentation.animelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.yumedev.seijakulistkmp.core.utils.rememberToastManager
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.presentation.components.AnimatedSearchBar
import com.yumedev.seijakulistkmp.features.tracking.presentation.components.MediaListCard
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.AdjustmentsHorizontal
import dev.seyfarth.tablericons.outlined.Search
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import seijakulistkmp.shared.generated.resources.*

class UserAnimeListScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<UserAnimeListViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        UserAnimeListScreenContent(
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAnimeListScreenContent(
    uiState: UserAnimeListUiState,
    onEvent: (UserAnimeListEvent) -> Unit
) {
    val toastManager = rememberToastManager()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.my_anime_list)) },
                actions = {
                    IconButton(
                        onClick = { onEvent(UserAnimeListEvent.ToggleSearch) },
                        enabled = uiState.entries.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = TablerIcons.Outlined.Search,
                            contentDescription = stringResource(Res.string.search)
                        )
                    }
                    IconButton(onClick = { /* TODO: Implement filter/sort */ }) {
                        Icon(
                            imageVector = TablerIcons.Outlined.AdjustmentsHorizontal,
                            contentDescription = stringResource(Res.string.filter_menu)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedSearchBar(
                visible = uiState.isSearchVisible,
                query = uiState.searchQuery,
                onQueryChange = { onEvent(UserAnimeListEvent.Search(it)) },
                onDismiss = { onEvent(UserAnimeListEvent.HideSearch) },
                placeholderRes = Res.string.search_anime_placeholder
            )

            StatusFilterChips(
                stats = uiState.stats,
                selectedStatus = uiState.selectedStatus,
                onStatusSelected = { status ->
                    onEvent(UserAnimeListEvent.FilterByStatus(status))
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (uiState.isSearchVisible) {
                            focusManager.clearFocus()
                        }
                    }
            ) {
                when {
                    uiState.isLoading && uiState.entries.isEmpty() -> {
                        LoadingState()
                    }
                    uiState.error != null -> {
                        ErrorState(
                            error = uiState.error,
                            onRetry = { onEvent(UserAnimeListEvent.Refresh) }
                        )
                    }
                    uiState.filteredEntries.isEmpty() -> {
                        EmptyState(
                            selectedStatus = uiState.selectedStatus
                        )
                    }
                    else -> {
                        AnimeListContent(
                            entries = uiState.filteredEntries,
                            onEvent = onEvent
                        )
                    }
                }
            }
        }
    }

    val deleteSuccessMessage = stringResource(Res.string.list_deleted_success)
    val statusUpdatedMessage = stringResource(Res.string.list_status_updated)

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { messageKey ->
            val message = when (messageKey) {
                "list_deleted_success" -> deleteSuccessMessage
                "list_status_updated" -> statusUpdatedMessage
                else -> messageKey
            }
            toastManager.showToast(message)
            onEvent(UserAnimeListEvent.ClearSuccessMessage)
        }
    }
}

@Composable
private fun StatusFilterChips(
    stats: com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStats?,
    selectedStatus: MediaListStatus?,
    onStatusSelected: (MediaListStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedStatus == null,
                onClick = { onStatusSelected(null) },
                label = {
                    Text(
                        text = if (stats != null) {
                            "${stringResource(Res.string.all)} • ${stats.totalEntries}"
                        } else {
                            stringResource(Res.string.all)
                        }
                    )
                }
            )
        }

        val statusItems = listOf(
            MediaListStatus.CURRENT to Res.string.list_status_watching_short,
            MediaListStatus.COMPLETED to Res.string.list_status_completed,
            MediaListStatus.PLANNING to Res.string.list_status_plan_to_watch_short,
            MediaListStatus.PAUSED to Res.string.list_status_paused,
            MediaListStatus.DROPPED to Res.string.list_status_dropped,
            MediaListStatus.REPEATING to Res.string.list_status_repeating
        )

        items(
            items = statusItems,
            key = { it.first }
        ) { (status, labelRes) ->
            val count = stats?.let {
                when (status) {
                    MediaListStatus.CURRENT -> it.currentCount
                    MediaListStatus.COMPLETED -> it.completedCount
                    MediaListStatus.PLANNING -> it.planningCount
                    MediaListStatus.PAUSED -> it.pausedCount
                    MediaListStatus.DROPPED -> it.droppedCount
                    MediaListStatus.REPEATING -> it.repeatingCount
                }
            }

            FilterChip(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(status) },
                label = {
                    Text(
                        text = if (count != null && count > 0) {
                            "${stringResource(labelRes)} • $count"
                        } else {
                            stringResource(labelRes)
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.error),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Button(onClick = onRetry) {
                Text(stringResource(Res.string.retry))
            }
        }
    }
}

@Composable
private fun EmptyState(
    selectedStatus: MediaListStatus?
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (selectedStatus != null) {
                    stringResource(Res.string.no_items_in_category)
                } else {
                    stringResource(Res.string.empty_anime_list)
                },
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            if (selectedStatus == null) {
                Text(
                    text = stringResource(Res.string.empty_list_hint_anime),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun AnimeListContent(
    entries: List<com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry>,
    onEvent: (UserAnimeListEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(entries, key = { it.id }) { entry ->
            MediaListCard(
                entry = entry,
                onIncrementProgress = {
                    onEvent(UserAnimeListEvent.IncrementProgress(entry.mediaId))
                },
                onEditClick = {
                    onEvent(UserAnimeListEvent.EditEntry(entry))
                },
                onStatusChange = { newStatus ->
                    onEvent(UserAnimeListEvent.ChangeStatus(entry.mediaId, newStatus))
                },
                onDeleteClick = {
                    onEvent(UserAnimeListEvent.RemoveEntry(entry.mediaId))
                }
            )
        }
    }
}

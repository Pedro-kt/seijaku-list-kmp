package com.yumedev.seijakulistkmp.features.search.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.core.domain.model.MediaType as CoreMediaType
import com.yumedev.seijakulistkmp.core.error.ErrorType
import com.yumedev.seijakulistkmp.core.error.ErrorUiMapper
import com.yumedev.seijakulistkmp.features.detail.presentation.components.AddToListBottomSheet
import com.yumedev.seijakulistkmp.features.search.presentation.SearchState
import com.yumedev.seijakulistkmp.features.search.presentation.SearchViewModel
import com.yumedev.seijakulistkmp.features.search.presentation.model.CharacterResultItem
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchResultItem
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowLeft
import dev.seyfarth.tablericons.outlined.MoodEmpty
import dev.seyfarth.tablericons.outlined.Search
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun SearchResultsContent(
    query: String,
    results: List<SearchResultItem>,
    characterResults: List<CharacterResultItem> = emptyList(),
    isLoading: Boolean,
    error: ErrorType?,
    state: SearchState,
    viewModel: SearchViewModel,
    onResultClick: (SearchResultItem) -> Unit,
    onCharacterClick: (CharacterResultItem) -> Unit = {},
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onSaveClick: (SearchResultItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        SearchResultsTopBar(
            query = query,
            onBackClick = onBackClick
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = stringResource(Res.string.search_searching),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            error != null -> {
                val (title, hint) = when (error) {
                    ErrorType.ServerUnavailable -> {
                        stringResource(Res.string.error_server_unavailable) to
                        stringResource(Res.string.error_server_unavailable_hint)
                    }
                    else -> {
                        stringResource(Res.string.error_search_failed) to
                        stringResource(ErrorUiMapper.mapToStringResource(error))
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            imageVector = TablerIcons.Outlined.MoodEmpty,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = hint,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Button(onClick = onRetry) {
                            Text(text = stringResource(Res.string.retry))
                        }
                    }
                }
            }
            results.isEmpty() && characterResults.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            imageVector = TablerIcons.Outlined.Search,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(Res.string.search_no_results),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(Res.string.search_no_results_hint),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            characterResults.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(Res.string.search_results_count, characterResults.size),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }

                    items(
                        items = characterResults,
                        key = { it.id }
                    ) { character ->
                        CharacterResultItem(
                            item = character,
                            onClick = { onCharacterClick(character) },
                            modifier = Modifier.animateItem(
                                fadeInSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                ),
                                placementSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(Res.string.search_results_count, results.size),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }

                    items(
                        items = results,
                        key = { it.id }
                    ) { result ->
                        SearchResultItem(
                            item = result,
                            onClick = { onResultClick(result) },
                            onSaveClick = { item ->
                                onSaveClick(item)
                            },
                            isSaved = false,
                            modifier = Modifier.animateItem(
                                fadeInSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                ),
                                placementSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }

    if (state.showAddToListBottomSheet && state.selectedItemForList != null) {
        val selectedItem = state.selectedItemForList
        AddToListBottomSheet(
            mediaTitle = selectedItem.title,
            mediaType = when (selectedItem.mediaType) {
                com.yumedev.seijakulistkmp.features.search.presentation.model.MediaType.ANIME -> CoreMediaType.ANIME
                com.yumedev.seijakulistkmp.features.search.presentation.model.MediaType.MANGA -> CoreMediaType.MANGA
            },
            mediaStatus = selectedItem.status,
            totalEpisodes = selectedItem.episodes,
            totalChapters = selectedItem.chapters,
            currentProgress = 0,
            currentScore = null,
            currentNote = "",
            currentStatus = null,
            currentStartDate = null,
            currentRewatches = 0,
            currentPriority = MediaListPriority.MEDIUM,
            onDismiss = { viewModel.hideAddToListBottomSheet() },
            onSave = { status, progress, score, note, startDate, rewatches, priority ->
                viewModel.saveToList(status, progress, score, note, startDate, rewatches, priority)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResultsTopBar(
    query: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = TablerIcons.Outlined.ArrowLeft,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Column {
                Text(
                    text = stringResource(Res.string.search_results_for),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = query,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    )
}

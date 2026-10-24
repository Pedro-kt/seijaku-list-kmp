package com.yumedev.seijakulistkmp.features.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode
import com.yumedev.seijakulistkmp.features.profile.presentation.model.AnimeGroup
import com.yumedev.seijakulistkmp.features.profile.presentation.model.EpisodeSortOption
import com.yumedev.seijakulistkmp.features.tracking.presentation.components.AnimatedSearchBar
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Search
import dev.seyfarth.tablericons.outlined.Star
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.profile_favorite_episodes_empty
import seijakulistkmp.shared.generated.resources.profile_favorite_episodes_title
import seijakulistkmp.shared.generated.resources.search_episodes_placeholder

@Composable
fun FavoriteEpisodesSection(
    groupedEpisodes: List<AnimeGroup>,
    animeTitles: Map<Int, String>,
    isLoading: Boolean,
    isSearchVisible: Boolean,
    searchQuery: String,
    currentSort: EpisodeSortOption,
    expandedGroups: Set<Int>,
    onEpisodeClick: (FavoriteEpisode) -> Unit,
    onToggleSearch: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSortChange: (EpisodeSortOption) -> Unit,
    onToggleGroup: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.profile_favorite_episodes_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                EpisodeSortDropdown(
                    currentSort = currentSort,
                    onSortChanged = onSortChange
                )

                IconButton(onClick = onToggleSearch) {
                    Icon(
                        imageVector = TablerIcons.Outlined.Search,
                        contentDescription = "Search episodes",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        AnimatedSearchBar(
            visible = isSearchVisible,
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            onDismiss = onToggleSearch,
            placeholderRes = Res.string.search_episodes_placeholder
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            groupedEpisodes.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = TablerIcons.Outlined.Star,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Text(
                            text = stringResource(Res.string.profile_favorite_episodes_empty),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    groupedEpisodes.forEach { group ->
                        AnimeGroupHeader(
                            animeTitle = animeTitles[group.mediaId] ?: "Anime ${group.mediaId}",
                            episodeCount = group.episodeCount,
                            isExpanded = expandedGroups.contains(group.mediaId),
                            onToggle = { onToggleGroup(group.mediaId) }
                        )

                        // Episodes (only if expanded)
                        if (expandedGroups.contains(group.mediaId)) {
                            group.episodes.forEach { episode ->
                                FavoriteEpisodeCard(
                                    episode = episode,
                                    animeTitle = animeTitles[episode.mediaId],
                                    onClick = { onEpisodeClick(episode) },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

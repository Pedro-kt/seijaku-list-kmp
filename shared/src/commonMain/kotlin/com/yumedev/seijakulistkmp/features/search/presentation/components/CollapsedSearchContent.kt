package com.yumedev.seijakulistkmp.features.search.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.search.presentation.SearchState
import com.yumedev.seijakulistkmp.features.search.presentation.model.Genre
import com.yumedev.seijakulistkmp.features.search.presentation.model.MoodFilter
import com.yumedev.seijakulistkmp.features.search.presentation.model.QuickFilter
import com.yumedev.seijakulistkmp.features.search.presentation.model.RecentSearch
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun CollapsedSearchContent(
    state: SearchState,
    onSearchClick: () -> Unit,
    onQuickFilterClick: (QuickFilter) -> Unit,
    onRecentSearchClick: (RecentSearch) -> Unit,
    onRemoveRecentSearch: (RecentSearch) -> Unit,
    onGenreClick: (Genre) -> Unit,
    onMoodClick: (genres: List<String>, moodName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

        CollapsedSearchBar(
            onSearchClick = onSearchClick,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                QuickFilterChips(
                    onFilterClick = onQuickFilterClick
                )
            }

            if (state.recentSearches.isNotEmpty()) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.search_recent_searches),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                items(
                    items = state.recentSearches,
                    key = { it.id }
                ) { search ->
                    RecentSearchItem(
                        search = search,
                        onSearchClick = { onRecentSearchClick(search) },
                        onRemoveClick = { onRemoveRecentSearch(search) },
                        showClockIcon = true
                    )
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.mood_what_are_you_looking_for),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = stringResource(Res.string.mood_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            item {
                MoodCards(
                    moods = MoodFilter.entries,
                    onMoodClick = onMoodClick
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.search_explore_by_genre),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    GenreChips(
                        genres = Genre.all(),
                        onGenreClick = onGenreClick
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

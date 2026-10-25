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
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

@Composable
fun CollapsedSearchContent(
    state: SearchState,
    onSearchClick: () -> Unit,
    onQuickFilterClick: (QuickFilter, String) -> Unit,
    onRecentSearchClick: (RecentSearch) -> Unit,
    onRemoveRecentSearch: (RecentSearch) -> Unit,
    onGenreClick: (Genre, String) -> Unit,
    onMoodClick: (genres: List<String>, moodName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

        CollapsedSearchBar(
            onSearchClick = onSearchClick,
            modifier = Modifier.padding(top = responsive.mediumSpacing)
        )

        Spacer(modifier = Modifier.height(responsive.mediumPadding))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(responsive.largePadding)
        ) {
            item {
                QuickFilterChips(
                    onFilterClick = onQuickFilterClick
                )
            }

            if (state.recentSearches.isNotEmpty()) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(responsive.mediumSpacing)
                    ) {
                        Text(
                            text = stringResource(Res.string.search_recent_searches),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = responsive.horizontalPadding)
                        )
                    }
                }

                items(
                    items = state.recentSearches.take(4),
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
                    verticalArrangement = Arrangement.spacedBy(responsive.mediumSpacing)
                ) {
                    Text(
                        text = stringResource(Res.string.mood_what_are_you_looking_for),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = responsive.horizontalPadding)
                    )
                    Text(
                        text = stringResource(Res.string.mood_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = responsive.horizontalPadding)
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
                    verticalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
                ) {
                    Text(
                        text = stringResource(Res.string.search_explore_by_genre),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = responsive.horizontalPadding)
                    )
                    GenreChips(
                        genres = Genre.all(),
                        onGenreClick = onGenreClick
                    )
                }
            }

        }
    }
}

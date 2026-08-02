package com.yumedev.seijakulistkmp.features.search.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.search.presentation.SearchState
import com.yumedev.seijakulistkmp.features.search.presentation.model.RecentSearch
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchFilter

@Composable
fun ExpandedSearchContent(
    state: SearchState,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSearch: () -> Unit,
    onFilterSelect: (SearchFilter) -> Unit,
    onFiltersClick: () -> Unit,
    onRecentSearchClick: (RecentSearch) -> Unit,
    onRemoveRecentSearch: (RecentSearch) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ExpandedSearchBar(
            query = state.searchQuery,
            onQueryChange = onQueryChange,
            onBackClick = onBackClick,
            onSearch = onSearch
        )

        SearchFilterChips(
            selectedFilter = state.selectedFilter,
            onFilterSelect = onFilterSelect,
            onFiltersClick = onFiltersClick,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        HorizontalDivider()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            if (state.recentSearches.isNotEmpty()) {
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
                Spacer(modifier = Modifier.height(8.dp))
            }

            val trendingSearches = listOf(
                RecentSearch(100, "solo leveling", System.currentTimeMillis()),
                RecentSearch(101, "dandadan", System.currentTimeMillis()),
                RecentSearch(102, "chainsaw man", System.currentTimeMillis()),
                RecentSearch(103, "monster", System.currentTimeMillis())
            )

            items(
                items = trendingSearches,
                key = { it.id }
            ) { search ->
                RecentSearchItem(
                    search = search,
                    onSearchClick = { onRecentSearchClick(search) },
                    onRemoveClick = { /* Navigate to detail */ },
                    showClockIcon = false
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

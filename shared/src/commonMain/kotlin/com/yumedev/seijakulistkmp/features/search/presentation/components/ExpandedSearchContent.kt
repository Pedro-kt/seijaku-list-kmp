package com.yumedev.seijakulistkmp.features.search.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.search.presentation.SearchState
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaFormat
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaStatus
import com.yumedev.seijakulistkmp.features.search.presentation.model.RecentSearch
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchFilter
import kotlinx.datetime.Clock

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
    onFilterStatusChange: (MediaStatus) -> Unit,
    onFilterFormatChange: (MediaFormat) -> Unit,
    onFilterMinScoreChange: (Int?) -> Unit,
    onResetFilters: () -> Unit,
    onApplyFilters: () -> Unit,
    modifier: Modifier = Modifier,
    autoFocus: Boolean = false
) {
    var showFilterSheet by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
        ExpandedSearchBar(
            query = state.searchQuery,
            onQueryChange = onQueryChange,
            onBackClick = onBackClick,
            onSearch = onSearch,
            autoFocus = autoFocus
        )

        SearchFilterChips(
            selectedFilter = state.selectedFilter,
            onFilterSelect = onFilterSelect,
            onFiltersClick = { showFilterSheet = true },
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
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
            }

            val now = Clock.System.now().toEpochMilliseconds()
            val trendingSearches = listOf(
                RecentSearch(100, "solo leveling", now),
                RecentSearch(101, "dandadan", now),
                RecentSearch(102, "chainsaw man", now),
                RecentSearch(103, "monster", now)
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

    if (showFilterSheet) {
        SearchFilterBottomSheet(
            mediaType = state.selectedFilter,
            selectedStatus = state.filterStatus,
            selectedFormat = state.filterFormat,
            minScore = state.filterMinScore,
            onStatusChange = onFilterStatusChange,
            onFormatChange = onFilterFormatChange,
            onMinScoreChange = onFilterMinScoreChange,
            onReset = onResetFilters,
            onApply = onApplyFilters,
            onDismiss = { showFilterSheet = false }
        )
    }
    }
}

package com.yumedev.seijakulistkmp.features.search.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.yumedev.seijakulistkmp.features.search.presentation.components.CollapsedSearchContent
import com.yumedev.seijakulistkmp.features.search.presentation.components.ExpandedSearchContent
import com.yumedev.seijakulistkmp.features.search.presentation.components.SearchResultsContent
import org.koin.compose.viewmodel.koinViewModel

class SearchScreen : Screen {
    @Composable
    override fun Content() {
        SearchScreenContent()
    }
}

@Composable
fun SearchScreenContent(
    shouldExpandOnStart: Boolean = false,
    onExpandedChange: (Boolean) -> Unit = {},
    onExpandHandled: () -> Unit = {}
) {
    val viewModel = koinViewModel<SearchViewModel>()
    val state by viewModel.state.collectAsState()

    var hasHandledNavigation by remember { mutableStateOf(false) }

    val isExpanded = if (shouldExpandOnStart && !hasHandledNavigation) {
        true
    } else {
        state.isExpanded
    }

    LaunchedEffect(shouldExpandOnStart) {
        if (shouldExpandOnStart && !hasHandledNavigation) {
            viewModel.setExpandedState(true)
            onExpandHandled()
            hasHandledNavigation = true
        }
    }

    LaunchedEffect(state.isExpanded) {
        onExpandedChange(state.isExpanded)
    }

    when {
        state.hasSearched -> {
            SearchResultsContent(
                query = state.searchQuery,
                results = state.searchResults,
                isLoading = state.isSearching,
                error = state.searchError,
                onResultClick = { result ->
                    // TODO: Navigate to anime detail
                },
                onBackClick = {
                    viewModel.clearSearchResults()
                },
                onRetry = {
                    viewModel.retrySearch()
                }
            )
        }
        isExpanded -> {
            ExpandedSearchContent(
            state = state,
            onQueryChange = { query ->
                viewModel.updateSearchQuery(query)
            },
            onBackClick = {
                viewModel.collapseSearch()
            },
            onSearch = {
                if (state.searchQuery.isNotBlank()) {
                    viewModel.performSearch(state.searchQuery)
                }
            },
            onFilterSelect = { filter ->
                viewModel.selectFilter(filter)
            },
            onFiltersClick = {
                // TODO: Open advanced filters dialog
            },
            onRecentSearchClick = { search ->
                viewModel.updateSearchQuery(search.query)
                viewModel.performSearch(search.query)
            },
            onRemoveRecentSearch = { search ->
                viewModel.removeRecentSearch(search)
            },
            autoFocus = true
        )
        }
        else -> {
            CollapsedSearchContent(
            state = state,
            onSearchClick = {
                viewModel.expandSearch()
            },
            onQuickFilterClick = { filter ->
                // TODO: Navigate to filtered list
            },
            onRecentSearchClick = { search ->
                viewModel.performSearch(search.query)
            },
            onRemoveRecentSearch = { search ->
                viewModel.removeRecentSearch(search)
            },
            onGenreClick = { genre ->
                // TODO: Navigate to genre list
            },
            onTrendingAnimeClick = { anime ->
                // TODO: Navigate to anime detail
            }
        )
        }
    }
}

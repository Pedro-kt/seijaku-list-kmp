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
import com.yumedev.seijakulistkmp.features.search.presentation.model.toApiValue
import org.koin.compose.viewmodel.koinViewModel
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaType as SearchMediaType

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
    onExpandHandled: () -> Unit = {},
    onNavigateToAnimeDetail: (Int) -> Unit = {},
    onNavigateToMangaDetail: (Int) -> Unit = {}
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

    LaunchedEffect(state.isExpanded, state.hasSearched) {
        onExpandedChange(state.isExpanded || state.hasSearched)
    }

    when {
        state.hasSearched -> {
            SearchResultsContent(
                query = state.searchQuery,
                results = state.searchResults,
                characterResults = state.characterResults,
                isLoading = state.isSearching,
                error = state.searchError,
                onResultClick = { result ->
                    when (result.mediaType) {
                        SearchMediaType.ANIME -> {
                            onNavigateToAnimeDetail(result.id)
                        }
                        SearchMediaType.MANGA -> {
                            onNavigateToMangaDetail(result.id)
                        }
                    }
                },
                onCharacterClick = { character ->
                    // TODO: Navigate to character detail
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
                // Handled internally by ExpandedSearchContent
            },
            onRecentSearchClick = { search ->
                viewModel.updateSearchQuery(search.query)
                viewModel.performSearch(search.query)
            },
            onRemoveRecentSearch = { search ->
                viewModel.removeRecentSearch(search)
            },
            onFilterStatusChange = { status ->
                viewModel.updateFilterStatus(status)
            },
            onFilterFormatChange = { format ->
                viewModel.updateFilterFormat(format)
            },
            onFilterMinScoreChange = { score ->
                viewModel.updateFilterMinScore(score)
            },
            onResetFilters = {
                viewModel.resetFilters()
            },
            onApplyFilters = {
                // Filters are already updated, could trigger search here if needed
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
            onQuickFilterClick = { filter, filterLabel ->
                viewModel.searchByQuickFilter(filter, filterLabel)
            },
            onRecentSearchClick = { search ->
                viewModel.performSearch(search.query)
            },
            onRemoveRecentSearch = { search ->
                viewModel.removeRecentSearch(search)
            },
            onGenreClick = { genre, genreLabel ->
                viewModel.searchByMood(listOf(genre.toApiValue()), genreLabel)
            },
            onMoodClick = { genres, moodName ->
                viewModel.searchByMood(genres, moodName)
            }
        )
        }
    }
}

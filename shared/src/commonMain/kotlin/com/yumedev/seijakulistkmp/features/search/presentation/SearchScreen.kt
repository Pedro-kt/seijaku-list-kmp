package com.yumedev.seijakulistkmp.features.search.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.presentation.DetailScreen
import com.yumedev.seijakulistkmp.features.search.presentation.components.CollapsedSearchContent
import com.yumedev.seijakulistkmp.features.search.presentation.components.ExpandedSearchContent
import com.yumedev.seijakulistkmp.features.search.presentation.components.SearchResultsContent
import com.yumedev.seijakulistkmp.features.search.presentation.model.QuickFilter
import com.yumedev.seijakulistkmp.features.search.presentation.model.toApiValue
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaType as SearchMediaType

class SearchScreen(
    private val quickFilter: QuickFilter? = null
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        SearchScreenContent(
            quickFilter = quickFilter,
            onNavigateToAnimeDetail = { animeId ->
                navigator.push(
                    DetailScreen(
                        animeId,
                        MediaType.ANIME
                    )
                )
            },
            onNavigateToMangaDetail = { mangaId ->
                navigator.push(
                    DetailScreen(
                        mangaId,
                        MediaType.MANGA
                    )
                )
            }
        )
    }
}

@Composable
fun SearchScreenContent(
    shouldExpandOnStart: Boolean = false,
    quickFilter: QuickFilter? = null,
    onExpandedChange: (Boolean) -> Unit = {},
    onExpandHandled: () -> Unit = {},
    onNavigateToAnimeDetail: (Int) -> Unit = {},
    onNavigateToMangaDetail: (Int) -> Unit = {}
) {
    val viewModel = koinViewModel<SearchViewModel>()
    val state by viewModel.state.collectAsState()
    val navigator = LocalNavigator.current

    var hasHandledNavigation by remember { mutableStateOf(false) }
    var hasExecutedQuickFilter by remember { mutableStateOf(false) }

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

    // Ejecutar Quick Filter al inicio
    LaunchedEffect(quickFilter) {
        if (quickFilter != null && !hasExecutedQuickFilter) {
            val filterLabel = when (quickFilter) {
                QuickFilter.CurrentSeason -> "Current Season"
                QuickFilter.AiringToday -> "Airing Today"
                QuickFilter.Top100 -> "Top 100"
                QuickFilter.Random -> "Random"
            }

            delay(100)
            viewModel.searchByQuickFilter(quickFilter, filterLabel)
        }
    }

    LaunchedEffect(state.isExpanded, state.hasSearched) {
        onExpandedChange(state.isExpanded || state.hasSearched)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        when {
        state.hasSearched -> {
            SearchResultsContent(
                query = state.searchQuery,
                results = state.searchResults,
                characterResults = state.characterResults,
                isLoading = state.isSearching,
                error = state.searchError,
                state = state,
                viewModel = viewModel,
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
                    if (quickFilter != null) {
                        navigator?.pop()
                    } else {
                        viewModel.clearSearchResults()
                    }
                },
                onRetry = {
                    viewModel.retrySearch()
                },
                onSaveClick = { item ->
                    viewModel.showAddToListBottomSheet(item)
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
}

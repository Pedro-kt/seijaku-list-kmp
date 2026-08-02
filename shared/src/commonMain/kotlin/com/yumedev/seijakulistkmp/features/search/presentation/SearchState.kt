package com.yumedev.seijakulistkmp.features.search.presentation

import com.yumedev.seijakulistkmp.core.error.ErrorType
import com.yumedev.seijakulistkmp.features.search.presentation.model.RecentSearch
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchFilter
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchResultItem
import com.yumedev.seijakulistkmp.features.search.presentation.model.TrendingAnime

data class SearchState(
    val isExpanded: Boolean = false,
    val searchQuery: String = "",
    val selectedFilter: SearchFilter = SearchFilter.ANIME,
    val recentSearches: List<RecentSearch> = emptyList(),
    val trendingAnimes: List<TrendingAnime> = emptyList(),
    val isLoadingTrending: Boolean = false,
    val trendingError: ErrorType? = null,
    // Search results
    val searchResults: List<SearchResultItem> = emptyList(),
    val isSearching: Boolean = false,
    val searchError: ErrorType? = null,
    val hasSearched: Boolean = false
)

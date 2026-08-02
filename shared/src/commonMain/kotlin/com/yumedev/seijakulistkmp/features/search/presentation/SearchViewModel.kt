package com.yumedev.seijakulistkmp.features.search.presentation

import androidx.lifecycle.ViewModel
import com.yumedev.seijakulistkmp.features.search.presentation.model.RecentSearch
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchFilter
import com.yumedev.seijakulistkmp.features.search.presentation.model.TrendingAnime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock

class SearchViewModel : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        val now = Clock.System.now().toEpochMilliseconds()

        // Mock recent searches
        val mockRecentSearches = listOf(
            RecentSearch(1, "frieren", now - 3600000),
            RecentSearch(2, "berserk", now - 7200000),
            RecentSearch(3, "maomao", now - 10800000),
            RecentSearch(4, "one piece", now - 14400000)
        )

        // Mock trending animes
        val mockTrendingAnimes = listOf(
            TrendingAnime(
                id = 1,
                rank = 1,
                title = "Frieren: Beyond Journey's End",
                coverImage = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx154587-n5xn4UGhKjhz.jpg",
                rating = 9.31,
                type = "TV",
                episodes = 28
            ),
            TrendingAnime(
                id = 2,
                rank = 2,
                title = "Fullmetal Alchemist: Brotherhood",
                coverImage = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx6702-VI6KCbmFLAW6.jpg",
                rating = 9.09,
                type = "TV",
                episodes = 64
            ),
            TrendingAnime(
                id = 3,
                rank = 3,
                title = "Steins;Gate",
                coverImage = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx9253-7pdcVzQSkKxT.jpg",
                rating = 9.07,
                type = "TV",
                episodes = 24
            ),
            TrendingAnime(
                id = 4,
                rank = 4,
                title = "Gintama°",
                coverImage = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx20614-KK5sTdkIgJSo.jpg",
                rating = 9.06,
                type = "TV",
                episodes = 51
            ),
            TrendingAnime(
                id = 5,
                rank = 5,
                title = "Hunter x Hunter (2011)",
                coverImage = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx11061-0l5gpHD4TA2d.png",
                rating = 9.04,
                type = "TV",
                episodes = 148
            )
        )

        _state.update {
            it.copy(
                recentSearches = mockRecentSearches,
                trendingAnimes = mockTrendingAnimes
            )
        }
    }

    fun expandSearch() {
        _state.update { it.copy(isExpanded = true) }
    }

    fun collapseSearch() {
        _state.update { it.copy(isExpanded = false, searchQuery = "") }
    }

    fun setExpandedState(expanded: Boolean) {
        _state.update { it.copy(isExpanded = expanded) }
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun selectFilter(filter: SearchFilter) {
        _state.update { it.copy(selectedFilter = filter) }
    }

    fun removeRecentSearch(search: RecentSearch) {
        _state.update {
            it.copy(recentSearches = it.recentSearches.filter { recent -> recent.id != search.id })
        }
    }

    fun addRecentSearch(query: String) {
        if (query.isBlank()) return

        val now = Clock.System.now().toEpochMilliseconds()
        val newSearch = RecentSearch(
            id = now,
            query = query,
            timestamp = now
        )

        _state.update {
            val updatedSearches = listOf(newSearch) + it.recentSearches.filter { recent ->
                recent.query.lowercase() != query.lowercase()
            }
            it.copy(recentSearches = updatedSearches.take(10))
        }
    }

    fun performSearch(query: String) {
        addRecentSearch(query)
        // TODO: Navigate to search results screen or show results
    }
}

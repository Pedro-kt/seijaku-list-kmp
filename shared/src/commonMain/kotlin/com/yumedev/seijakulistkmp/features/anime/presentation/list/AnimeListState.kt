package com.yumedev.seijakulistkmp.features.anime.presentation.list

import com.yumedev.seijakulistkmp.core.common.resource.Resource
import com.yumedev.seijakulistkmp.features.anime.domain.model.Anime

/**
 * UI State for Anime List screen
 */
data class AnimeListState(
    val animes: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
    val isLoadingMore: Boolean = false,
    val searchQuery: String = "",
    val selectedTab: AnimeListTab = AnimeListTab.TRENDING
)

/**
 * Tabs for anime list
 */
enum class AnimeListTab {
    TRENDING,
    POPULAR,
    SEARCH
}

/**
 * Events that can occur in the Anime List screen
 */
sealed class AnimeListEvent {
    data object LoadAnimes : AnimeListEvent()
    data object LoadMore : AnimeListEvent()
    data class SearchAnime(val query: String) : AnimeListEvent()
    data class SelectTab(val tab: AnimeListTab) : AnimeListEvent()
    data object Refresh : AnimeListEvent()
}

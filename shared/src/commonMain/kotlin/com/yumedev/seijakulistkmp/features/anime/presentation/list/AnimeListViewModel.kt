package com.yumedev.seijakulistkmp.features.anime.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.common.resource.Resource
import com.yumedev.seijakulistkmp.features.anime.domain.usecase.GetTrendingAnimeUseCase
import com.yumedev.seijakulistkmp.features.anime.domain.usecase.SearchAnimeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Anime List screen
 * Manages UI state and business logic
 */
class AnimeListViewModel(
    private val getTrendingAnimeUseCase: GetTrendingAnimeUseCase,
    private val searchAnimeUseCase: SearchAnimeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AnimeListState())
    val state: StateFlow<AnimeListState> = _state.asStateFlow()

    init {
        loadAnimes()
    }

    fun onEvent(event: AnimeListEvent) {
        when (event) {
            is AnimeListEvent.LoadAnimes -> loadAnimes()
            is AnimeListEvent.LoadMore -> loadMore()
            is AnimeListEvent.SearchAnime -> searchAnime(event.query)
            is AnimeListEvent.SelectTab -> selectTab(event.tab)
            is AnimeListEvent.Refresh -> refresh()
        }
    }

    private fun loadAnimes() {
        viewModelScope.launch {
            when (_state.value.selectedTab) {
                AnimeListTab.TRENDING -> loadTrendingAnimes()
                AnimeListTab.POPULAR -> loadPopularAnimes()
                AnimeListTab.SEARCH -> {
                    if (_state.value.searchQuery.isNotBlank()) {
                        searchAnime(_state.value.searchQuery)
                    }
                }
            }
        }
    }

    private suspend fun loadTrendingAnimes() {
        getTrendingAnimeUseCase(page = 1, perPage = 20).collect { result ->
            when (result) {
                is com.yumedev.seijakulistkmp.core.common.resource.Result.Success -> {
                    _state.update {
                        it.copy(
                            animes = result.data.data,
                            isLoading = false,
                            error = null,
                            currentPage = result.data.pagination.currentPage,
                            hasNextPage = result.data.pagination.hasNextPage
                        )
                    }
                }
                is com.yumedev.seijakulistkmp.core.common.resource.Result.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.message
                        )
                    }
                }
            }
        }
    }

    private suspend fun loadPopularAnimes() {
        // Similar implementation - to be added when we create GetPopularAnimeUseCase
        // For now, using trending as placeholder
        loadTrendingAnimes()
    }

    private fun loadMore() {
        if (!_state.value.hasNextPage || _state.value.isLoadingMore) return

        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true) }

            val nextPage = _state.value.currentPage + 1
            getTrendingAnimeUseCase(page = nextPage, perPage = 20).collect { result ->
                when (result) {
                    is com.yumedev.seijakulistkmp.core.common.resource.Result.Success -> {
                        _state.update {
                            it.copy(
                                animes = it.animes + result.data.data,
                                isLoadingMore = false,
                                currentPage = result.data.pagination.currentPage,
                                hasNextPage = result.data.pagination.hasNextPage
                            )
                        }
                    }
                    is com.yumedev.seijakulistkmp.core.common.resource.Result.Failure -> {
                        _state.update {
                            it.copy(
                                isLoadingMore = false,
                                error = result.error.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun searchAnime(query: String) {
        _state.update { it.copy(searchQuery = query, isLoading = true) }

        viewModelScope.launch {
            searchAnimeUseCase(query = query, page = 1, perPage = 20).collect { result ->
                when (result) {
                    is com.yumedev.seijakulistkmp.core.common.resource.Result.Success -> {
                        _state.update {
                            it.copy(
                                animes = result.data.data,
                                isLoading = false,
                                error = null,
                                currentPage = result.data.pagination.currentPage,
                                hasNextPage = result.data.pagination.hasNextPage
                            )
                        }
                    }
                    is com.yumedev.seijakulistkmp.core.common.resource.Result.Failure -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.error.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun selectTab(tab: AnimeListTab) {
        _state.update { it.copy(selectedTab = tab, currentPage = 1) }
        loadAnimes()
    }

    private fun refresh() {
        _state.update { it.copy(currentPage = 1, animes = emptyList()) }
        loadAnimes()
    }
}

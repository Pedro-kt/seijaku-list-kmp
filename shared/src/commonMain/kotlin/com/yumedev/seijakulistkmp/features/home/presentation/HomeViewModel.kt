package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.common.resource.Resource
import com.yumedev.seijakulistkmp.features.home.domain.usecase.*
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getFeaturedAnimeUseCase: GetFeaturedAnimeUseCase,
    private val getAiringNowAnimeUseCase: GetAiringNowAnimeUseCase,
    private val getNextSeasonAnimeUseCase: GetNextSeasonAnimeUseCase,
    private val getTopRatedAnimeUseCase: GetTopRatedAnimeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadAllSections()
    }

    private fun loadAllSections() {
        loadFeaturedAnime()
        loadAiringNow()
        loadNextSeason()
        loadTopRated()
    }

    private fun loadFeaturedAnime() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingFeatured = true, featuredError = null) }

            getFeaturedAnimeUseCase(limit = 5).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val featuredItems = resource.data.map { dto ->
                            dto.toFeaturedMediaItem()
                        }

                        _state.update {
                            it.copy(
                                featuredAnime = featuredItems,
                                isLoadingFeatured = false,
                                featuredError = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoadingFeatured = false,
                                featuredError = resource.message
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update { it.copy(isLoadingFeatured = true) }
                    }

                    is Resource.Idle -> {
                        // Do nothing, initial state
                    }
                }
            }
        }
    }

    private fun loadAiringNow() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingAiringNow = true, airingNowError = null) }

            val result = getAiringNowAnimeUseCase(page = 1, perPage = 10)

            result.onSuccess { animeList ->
                val cardItems = animeList.map { it.toAnimeCardItem() }
                _state.update {
                    it.copy(
                        airingNowAnime = cardItems,
                        isLoadingAiringNow = false,
                        airingNowError = null
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoadingAiringNow = false,
                        airingNowError = error.message
                    )
                }
            }
        }
    }

    private fun loadNextSeason() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingNextSeason = true, nextSeasonError = null) }

            val result = getNextSeasonAnimeUseCase(page = 1, perPage = 10)

            result.onSuccess { animeList ->
                val cardItems = animeList.map { it.toAnimeCardItem() }
                _state.update {
                    it.copy(
                        nextSeasonAnime = cardItems,
                        isLoadingNextSeason = false,
                        nextSeasonError = null
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoadingNextSeason = false,
                        nextSeasonError = error.message
                    )
                }
            }
        }
    }

    private fun loadTopRated() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingTopRated = true, topRatedError = null) }

            val result = getTopRatedAnimeUseCase(page = 1, perPage = 10)

            result.onSuccess { animeList ->
                val cardItems = animeList.map { it.toAnimeCardItem() }
                _state.update {
                    it.copy(
                        topRatedAnime = cardItems,
                        isLoadingTopRated = false,
                        topRatedError = null
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoadingTopRated = false,
                        topRatedError = error.message
                    )
                }
            }
        }
    }

    fun retryLoadFeaturedAnime() = loadFeaturedAnime()
    fun retryLoadAiringNow() = loadAiringNow()
    fun retryLoadNextSeason() = loadNextSeason()
    fun retryLoadTopRated() = loadTopRated()
}

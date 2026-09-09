package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.common.resource.Resource
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetAiringNowAnimeUseCase
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetFeaturedAnimeUseCase
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetNextSeasonAnimeUseCase
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetTopRatedAnimeUseCase
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.ErrorUiMapper
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toAnimeCardItem
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toFeaturedMediaItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnimeHomeViewModel(
    private val getFeaturedAnimeUseCase: GetFeaturedAnimeUseCase,
    private val getAiringNowAnimeUseCase: GetAiringNowAnimeUseCase,
    private val getNextSeasonAnimeUseCase: GetNextSeasonAnimeUseCase,
    private val getTopRatedAnimeUseCase: GetTopRatedAnimeUseCase,
    private val mediaStringFormatter: MediaStringFormatter,
    private val errorUiMapper: ErrorUiMapper
) : ViewModel() {

    private val _state = MutableStateFlow(AnimeHomeState())
    val state: StateFlow<AnimeHomeState> = _state.asStateFlow()

    private var animeAutoScrollJob: Job? = null

    companion object {
        private const val AUTO_SCROLL_DELAY_MS = 5000L
    }

    init {
        loadAllSections()
    }

    private fun loadAllSections() {
        viewModelScope.launch {
            launch { loadFeaturedAnime() }
            launch { loadAiringNow() }
            launch { loadNextSeason() }
            launch { loadTopRated() }
        }
    }

    private suspend fun loadFeaturedAnime() {
        _state.update { it.copy(isLoadingFeaturedAnime = true, featuredAnimeError = null) }

        getFeaturedAnimeUseCase(limit = 5).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    val featuredItems = resource.data.map { dto ->
                        dto.toFeaturedMediaItem(mediaStringFormatter)
                    }

                    _state.update {
                        it.copy(
                            featuredAnime = featuredItems,
                            currentFeaturedAnimeIndex = 0,
                            isLoadingFeaturedAnime = false,
                            featuredAnimeError = null
                        )
                    }
                    startAnimeAutoScroll()
                }

                is Resource.Error -> {
                    val errorType = resource.exception?.let { ErrorMapper.mapToErrorType(it) }
                    val errorUi = errorType?.let { errorUiMapper.mapFeaturedError(it) }
                    _state.update {
                        it.copy(
                            isLoadingFeaturedAnime = false,
                            featuredAnimeError = errorUi
                        )
                    }
                }

                is Resource.Loading -> {
                    _state.update { it.copy(isLoadingFeaturedAnime = true) }
                }

                is Resource.Idle -> {
                }
            }
        }
    }

    private suspend fun loadAiringNow() {
        _state.update { it.copy(isLoadingAiringNow = true, airingNowError = null) }

        val result = getAiringNowAnimeUseCase(page = 1, perPage = 10)

        result.onSuccess { animeList ->
            val cardItems = animeList.map { anime ->
                anime.toAnimeCardItem(mediaStringFormatter)
            }
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

    private suspend fun loadNextSeason() {
        _state.update { it.copy(isLoadingNextSeason = true, nextSeasonError = null) }

        val result = getNextSeasonAnimeUseCase(page = 1, perPage = 10)

        result.onSuccess { animeList ->
            val cardItems = animeList.map { it.toAnimeCardItem(mediaStringFormatter) }
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

    private suspend fun loadTopRated() {
        _state.update { it.copy(isLoadingTopRated = true, topRatedError = null) }

        val result = getTopRatedAnimeUseCase(page = 1, perPage = 10)

        result.onSuccess { animeList ->
            val cardItems = animeList.map { it.toAnimeCardItem(mediaStringFormatter) }
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

    private fun startAnimeAutoScroll() {
        animeAutoScrollJob?.cancel()
        animeAutoScrollJob = viewModelScope.launch {
            while (true) {
                delay(AUTO_SCROLL_DELAY_MS)
                val currentState = _state.value
                if (currentState.featuredAnime.isNotEmpty()) {
                    val nextIndex = (currentState.currentFeaturedAnimeIndex + 1) % currentState.featuredAnime.size
                    _state.update { it.copy(currentFeaturedAnimeIndex = nextIndex) }
                }
            }
        }
    }

    fun onFeaturedAnimeInteraction() {
        startAnimeAutoScroll()
    }

    override fun onCleared() {
        super.onCleared()
        animeAutoScrollJob?.cancel()
    }

    fun refreshAll() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            val jobs = listOf(
                async { loadFeaturedAnime() },
                async { loadAiringNow() },
                async { loadNextSeason() },
                async { loadTopRated() }
            )

            jobs.forEach { it.await() }
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    fun retryLoadFeaturedAnime() = viewModelScope.launch { loadFeaturedAnime() }
    fun retryLoadAiringNow() = viewModelScope.launch { loadAiringNow() }
    fun retryLoadNextSeason() = viewModelScope.launch { loadNextSeason() }
    fun retryLoadTopRated() = viewModelScope.launch { loadTopRated() }

    fun retryAllAnime() {
        viewModelScope.launch {
            launch { loadFeaturedAnime() }
            launch { loadAiringNow() }
            launch { loadNextSeason() }
            launch { loadTopRated() }
        }
    }
}

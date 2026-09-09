package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetFeaturedMangaUseCase
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetManhwaMangaUseCase
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetPopularMangaUseCase
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetPublishingMangaUseCase
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetRecentlyAddedMangaUseCase
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetTopRatedMangaUseCase
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.ErrorUiMapper
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toFeaturedMediaItem
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toMangaCardItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MangaHomeViewModel(
    private val getFeaturedMangaUseCase: GetFeaturedMangaUseCase,
    private val getPublishingMangaUseCase: GetPublishingMangaUseCase,
    private val getPopularMangaUseCase: GetPopularMangaUseCase,
    private val getTopRatedMangaUseCase: GetTopRatedMangaUseCase,
    private val getRecentlyAddedMangaUseCase: GetRecentlyAddedMangaUseCase,
    private val getManhwaMangaUseCase: GetManhwaMangaUseCase,
    private val mediaStringFormatter: MediaStringFormatter,
    private val errorUiMapper: ErrorUiMapper
) : ViewModel() {

    private val _state = MutableStateFlow(MangaHomeState())
    val state: StateFlow<MangaHomeState> = _state.asStateFlow()

    private var mangaAutoScrollJob: Job? = null

    companion object {
        private const val AUTO_SCROLL_DELAY_MS = 5000L
    }

    init {
        loadAllSections()
    }

    private fun loadAllSections() {
        viewModelScope.launch {
            launch { loadFeaturedManga() }
            launch { loadPublishingManga() }
            launch { loadPopularManga() }
            launch { loadTopRatedManga() }
            launch { loadRecentlyAddedManga() }
            launch { loadManhwaManga() }
        }
    }

    private suspend fun loadFeaturedManga() {
        _state.update { it.copy(isLoadingFeaturedManga = true, featuredMangaError = null) }

        try {
            val result = getFeaturedMangaUseCase(page = 1, perPage = 5)

            result.onSuccess { mangaList ->
                val featuredItems = buildList {
                    mangaList.forEach { manga ->
                        add(manga.toFeaturedMediaItem(mediaStringFormatter))
                    }
                }
                _state.update {
                    it.copy(
                        featuredManga = featuredItems,
                        currentFeaturedMangaIndex = 0,
                        isLoadingFeaturedManga = false,
                        featuredMangaError = null
                    )
                }
                startMangaAutoScroll()
            }.onFailure { error ->
                val errorType = ErrorMapper.mapToErrorType(error)
                val errorUi = errorUiMapper.mapFeaturedError(errorType)
                _state.update {
                    it.copy(
                        isLoadingFeaturedManga = false,
                        featuredMangaError = errorUi
                    )
                }
            }
        } catch (e: Exception) {
            val errorType = ErrorMapper.mapToErrorType(e)
            val errorUi = errorUiMapper.mapFeaturedError(errorType)
            _state.update {
                it.copy(
                    isLoadingFeaturedManga = false,
                    featuredMangaError = errorUi
                )
            }
        }
    }

    private suspend fun loadPublishingManga() {
        _state.update { it.copy(isLoadingPublishingManga = true, publishingMangaError = null) }

        val result = getPublishingMangaUseCase(page = 1, perPage = 10)

        result.onSuccess { mangaList ->
            val cardItems = mangaList.map { it.toMangaCardItem(mediaStringFormatter) }
            _state.update {
                it.copy(
                    publishingManga = cardItems,
                    isLoadingPublishingManga = false,
                    publishingMangaError = null
                )
            }
        }.onFailure { error ->
            _state.update {
                it.copy(
                    isLoadingPublishingManga = false,
                    publishingMangaError = error.message
                )
            }
        }
    }

    private suspend fun loadPopularManga() {
        _state.update { it.copy(isLoadingPopularManga = true, popularMangaError = null) }

        val result = getPopularMangaUseCase(page = 1, perPage = 10)

        result.onSuccess { mangaList ->
            val cardItems = mangaList.map { it.toMangaCardItem(mediaStringFormatter) }
            _state.update {
                it.copy(
                    popularManga = cardItems,
                    isLoadingPopularManga = false,
                    popularMangaError = null
                )
            }
        }.onFailure { error ->
            _state.update {
                it.copy(
                    isLoadingPopularManga = false,
                    popularMangaError = error.message
                )
            }
        }
    }

    private suspend fun loadTopRatedManga() {
        _state.update { it.copy(isLoadingTopRatedManga = true, topRatedMangaError = null) }

        val result = getTopRatedMangaUseCase(page = 1, perPage = 10)

        result.onSuccess { mangaList ->
            val cardItems = mangaList.map { it.toMangaCardItem(mediaStringFormatter) }
            _state.update {
                it.copy(
                    topRatedManga = cardItems,
                    isLoadingTopRatedManga = false,
                    topRatedMangaError = null
                )
            }
        }.onFailure { error ->
            _state.update {
                it.copy(
                    isLoadingTopRatedManga = false,
                    topRatedMangaError = error.message
                )
            }
        }
    }

    private suspend fun loadRecentlyAddedManga() {
        _state.update { it.copy(isLoadingRecentlyAddedManga = true, recentlyAddedMangaError = null) }

        val result = getRecentlyAddedMangaUseCase(page = 1, perPage = 10)

        result.onSuccess { mangaList ->
            val cardItems = mangaList.map { it.toMangaCardItem(mediaStringFormatter) }
            _state.update {
                it.copy(
                    recentlyAddedManga = cardItems,
                    isLoadingRecentlyAddedManga = false,
                    recentlyAddedMangaError = null
                )
            }
        }.onFailure { error ->
            _state.update {
                it.copy(
                    isLoadingRecentlyAddedManga = false,
                    recentlyAddedMangaError = error.message
                )
            }
        }
    }

    private suspend fun loadManhwaManga() {
        _state.update { it.copy(isLoadingManhwaManga = true, manhwaMangaError = null) }

        val result = getManhwaMangaUseCase(page = 1, perPage = 10, countryOfOrigin = "KR")

        result.onSuccess { mangaList ->
            val cardItems = mangaList.map { it.toMangaCardItem(mediaStringFormatter) }
            _state.update {
                it.copy(
                    manhwaManga = cardItems,
                    isLoadingManhwaManga = false,
                    manhwaMangaError = null
                )
            }
        }.onFailure { error ->
            _state.update {
                it.copy(
                    isLoadingManhwaManga = false,
                    manhwaMangaError = error.message
                )
            }
        }
    }

    private fun startMangaAutoScroll() {
        mangaAutoScrollJob?.cancel()
        mangaAutoScrollJob = viewModelScope.launch {
            while (true) {
                delay(AUTO_SCROLL_DELAY_MS)
                val currentState = _state.value
                if (currentState.featuredManga.isNotEmpty()) {
                    val nextIndex = (currentState.currentFeaturedMangaIndex + 1) % currentState.featuredManga.size
                    _state.update { it.copy(currentFeaturedMangaIndex = nextIndex) }
                }
            }
        }
    }

    fun onFeaturedMangaInteraction() {
        startMangaAutoScroll()
    }

    override fun onCleared() {
        super.onCleared()
        mangaAutoScrollJob?.cancel()
    }

    fun refreshAll() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            val jobs = listOf(
                async { loadFeaturedManga() },
                async { loadPublishingManga() },
                async { loadPopularManga() },
                async { loadTopRatedManga() },
                async { loadRecentlyAddedManga() },
                async { loadManhwaManga() }
            )

            jobs.forEach { it.await() }
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    fun retryLoadFeaturedManga() = viewModelScope.launch { loadFeaturedManga() }
    fun retryLoadPublishingManga() = viewModelScope.launch { loadPublishingManga() }
    fun retryLoadPopularManga() = viewModelScope.launch { loadPopularManga() }
    fun retryLoadTopRatedManga() = viewModelScope.launch { loadTopRatedManga() }
    fun retryLoadRecentlyAddedManga() = viewModelScope.launch { loadRecentlyAddedManga() }
    fun retryLoadManhwaManga() = viewModelScope.launch { loadManhwaManga() }

    fun retryAllManga() {
        viewModelScope.launch {
            launch { loadFeaturedManga() }
            launch { loadPublishingManga() }
            launch { loadPopularManga() }
            launch { loadTopRatedManga() }
            launch { loadRecentlyAddedManga() }
            launch { loadManhwaManga() }
        }
    }
}

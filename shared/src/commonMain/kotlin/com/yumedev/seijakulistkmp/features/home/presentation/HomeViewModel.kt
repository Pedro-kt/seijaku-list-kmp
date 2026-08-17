package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.common.resource.Resource
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.features.home.domain.usecase.*
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getFeaturedAnimeUseCase: GetFeaturedAnimeUseCase,
    private val getAiringNowAnimeUseCase: GetAiringNowAnimeUseCase,
    private val getNextSeasonAnimeUseCase: GetNextSeasonAnimeUseCase,
    private val getTopRatedAnimeUseCase: GetTopRatedAnimeUseCase,
    private val getFeaturedMangaUseCase: GetFeaturedMangaUseCase,
    private val getPublishingMangaUseCase: GetPublishingMangaUseCase,
    private val getPopularMangaUseCase: GetPopularMangaUseCase,
    private val getTopRatedMangaUseCase: GetTopRatedMangaUseCase,
    private val getRecentlyAddedMangaUseCase: GetRecentlyAddedMangaUseCase,
    private val getManhwaMangaUseCase: GetManhwaMangaUseCase,
    private val mediaStringFormatter: MediaStringFormatter,
    private val errorUiMapper: ErrorUiMapper
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private var animeAutoScrollJob: Job? = null
    private var mangaAutoScrollJob: Job? = null

    companion object {
        private const val AUTO_SCROLL_DELAY_MS = 5000L
    }

    init {
        loadAllSections()
    }

    private fun loadAllSections() {
        loadFeaturedAnime()
        loadAiringNow()
        loadNextSeason()
        loadTopRated()
        loadFeaturedManga()
        loadPublishingManga()
        loadPopularManga()
        loadTopRatedManga()
        loadRecentlyAddedManga()
        loadManhwaManga()
    }

    private fun loadFeaturedAnime() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingFeatured = true, featuredError = null) }

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
                                isLoadingFeatured = false,
                                featuredError = null
                            )
                        }
                        startAnimeAutoScroll()
                    }

                    is Resource.Error -> {
                        val errorType = resource.exception?.let { ErrorMapper.mapToErrorType(it) }
                        val errorUi = errorType?.let { errorUiMapper.mapFeaturedError(it) }
                        _state.update {
                            it.copy(
                                isLoadingFeatured = false,
                                featuredError = errorUi
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
    }

    private fun loadNextSeason() {
        viewModelScope.launch {
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
    }

    private fun loadTopRated() {
        viewModelScope.launch {
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
    }

    private fun loadFeaturedManga() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingFeatured = true, featuredError = null) }

            val result = getFeaturedMangaUseCase(page = 1, perPage = 5)

            result.onSuccess { mangaList ->
                val featuredItems = mangaList.map { it.toFeaturedMediaItem(mediaStringFormatter) }
                _state.update {
                    it.copy(
                        featuredManga = featuredItems,
                        currentFeaturedMangaIndex = 0,
                        isLoadingFeatured = false,
                        featuredError = null
                    )
                }
                startMangaAutoScroll()
            }.onFailure { error ->
                val errorType = ErrorMapper.mapToErrorType(error)
                val errorUi = errorUiMapper.mapFeaturedError(errorType)
                _state.update {
                    it.copy(
                        isLoadingFeatured = false,
                        featuredError = errorUi
                    )
                }
            }
        }
    }

    private fun loadPublishingManga() {
        viewModelScope.launch {
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
    }

    private fun loadPopularManga() {
        viewModelScope.launch {
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
    }

    private fun loadTopRatedManga() {
        viewModelScope.launch {
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
    }

    private fun loadRecentlyAddedManga() {
        viewModelScope.launch {
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
    }

    private fun loadManhwaManga() {
        viewModelScope.launch {
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

    fun onFeaturedAnimeInteraction() {
        startAnimeAutoScroll()
    }

    fun onFeaturedMangaInteraction() {
        startMangaAutoScroll()
    }

    override fun onCleared() {
        super.onCleared()
        animeAutoScrollJob?.cancel()
        mangaAutoScrollJob?.cancel()
    }

    fun refreshAll() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            loadAllSections()
            delay(500)
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    fun retryLoadFeaturedAnime() = loadFeaturedAnime()
    fun retryLoadAiringNow() = loadAiringNow()
    fun retryLoadNextSeason() = loadNextSeason()
    fun retryLoadTopRated() = loadTopRated()
    fun retryLoadFeaturedManga() = loadFeaturedManga()
    fun retryLoadPublishingManga() = loadPublishingManga()
    fun retryLoadPopularManga() = loadPopularManga()
    fun retryLoadTopRatedManga() = loadTopRatedManga()
    fun retryLoadRecentlyAddedManga() = loadRecentlyAddedManga()
    fun retryLoadManhwaManga() = loadManhwaManga()
}

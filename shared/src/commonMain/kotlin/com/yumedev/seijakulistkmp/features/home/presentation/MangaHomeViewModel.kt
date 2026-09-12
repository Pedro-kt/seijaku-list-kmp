package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetMangaHomeDataUseCase
import com.yumedev.seijakulistkmp.data.remote.graphql.GetMangaHomeDataQuery
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.ErrorUiMapper
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toFeaturedMediaItem
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toMangaCardItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MangaHomeViewModel(
    private val getMangaHomeDataUseCase: GetMangaHomeDataUseCase,
    private val mediaStringFormatter: MediaStringFormatter,
    private val errorUiMapper: ErrorUiMapper
) : ViewModel() {

    private val _state = MutableStateFlow(MangaHomeState())
    val state: StateFlow<MangaHomeState> = _state.asStateFlow()

    private var mangaAutoScrollJob: Job? = null
    private var hasLoadedData = false

    companion object {
        private const val AUTO_SCROLL_DELAY_MS = 5000L
    }

    fun loadAllSections() {
        if (hasLoadedData) return
        hasLoadedData = true

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isInitialLoading = true,
                    isLoadingFeaturedManga = true,
                    isLoadingPublishingManga = true,
                    isLoadingPopularManga = true,
                    isLoadingTopRatedManga = true,
                    isLoadingRecentlyAddedManga = true,
                    isLoadingManhwaManga = true,
                    featuredMangaError = null,
                    publishingMangaError = null,
                    popularMangaError = null,
                    topRatedMangaError = null,
                    recentlyAddedMangaError = null,
                    manhwaMangaError = null
                )
            }

            val result = getMangaHomeDataUseCase(isAdult = false)

            result.onSuccess { data ->
                val featuredItems = data.featured?.media?.mapNotNull { media ->
                    media?.toFeaturedMediaItem(mediaStringFormatter)
                } ?: emptyList()

                val publishingItems = data.publishing?.media?.mapNotNull { media ->
                    media?.toMangaCardItem(mediaStringFormatter)
                } ?: emptyList()

                val popularItems = data.popular?.media?.mapNotNull { media ->
                    media?.toMangaCardItem(mediaStringFormatter)
                } ?: emptyList()

                val topRatedItems = data.topRated?.media?.mapNotNull { media ->
                    media?.toMangaCardItem(mediaStringFormatter)
                } ?: emptyList()

                val recentlyAddedItems = data.recentlyAdded?.media?.mapNotNull { media ->
                    media?.toMangaCardItem(mediaStringFormatter)
                } ?: emptyList()

                val manhwaItems = data.manhwa?.media?.mapNotNull { media ->
                    media?.toMangaCardItem(mediaStringFormatter)
                } ?: emptyList()

                _state.update {
                    it.copy(
                        featuredManga = featuredItems,
                        currentFeaturedMangaIndex = 0,
                        publishingManga = publishingItems,
                        popularManga = popularItems,
                        topRatedManga = topRatedItems,
                        recentlyAddedManga = recentlyAddedItems,
                        manhwaManga = manhwaItems,
                        isInitialLoading = false,
                        isLoadingFeaturedManga = false,
                        isLoadingPublishingManga = false,
                        isLoadingPopularManga = false,
                        isLoadingTopRatedManga = false,
                        isLoadingRecentlyAddedManga = false,
                        isLoadingManhwaManga = false,
                        featuredMangaError = null,
                        publishingMangaError = null,
                        popularMangaError = null,
                        topRatedMangaError = null,
                        recentlyAddedMangaError = null,
                        manhwaMangaError = null
                    )
                }

                if (featuredItems.isNotEmpty()) {
                    startMangaAutoScroll()
                }
            }.onFailure { error ->
                val errorType = ErrorMapper.mapToErrorType(error)
                val errorUi = errorUiMapper.mapFeaturedError(errorType)

                _state.update {
                    it.copy(
                        isInitialLoading = false,
                        isLoadingFeaturedManga = false,
                        isLoadingPublishingManga = false,
                        isLoadingPopularManga = false,
                        isLoadingTopRatedManga = false,
                        isLoadingRecentlyAddedManga = false,
                        isLoadingManhwaManga = false,
                        featuredMangaError = errorUi
                    )
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

    fun onFeaturedMangaInteraction() {
        startMangaAutoScroll()
    }

    override fun onCleared() {
        super.onCleared()
        mangaAutoScrollJob?.cancel()
    }

    fun refreshAll() {
        hasLoadedData = false
        loadAllSections()
    }

    fun retryAllManga() {
        hasLoadedData = false
        loadAllSections()
    }
}

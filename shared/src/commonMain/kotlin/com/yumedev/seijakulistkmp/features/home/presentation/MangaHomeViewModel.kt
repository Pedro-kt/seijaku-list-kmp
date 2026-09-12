package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.GetMangaDetailUseCase
import com.yumedev.seijakulistkmp.features.detail.presentation.utils.toCore
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetMangaHomeDataUseCase
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.ErrorUiMapper
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toFeaturedMediaItem
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toMangaCardItem
import com.yumedev.seijakulistkmp.features.tracking.domain.model.CachedMediaInfo
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.AddToListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetListEntryUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.UpdateListEntryUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MangaHomeViewModel(
    private val getMangaHomeDataUseCase: GetMangaHomeDataUseCase,
    private val getMangaDetailUseCase: GetMangaDetailUseCase,
    private val addToListUseCase: AddToListUseCase,
    private val updateListEntryUseCase: UpdateListEntryUseCase,
    private val getListEntryUseCase: GetListEntryUseCase,
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

    fun onCardLongPress(mediaId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingBottomSheet = true) }

            val detailResult = getMangaDetailUseCase(mediaId)

            detailResult.onSuccess { mediaDetail ->
                val listEntryResult = getListEntryUseCase(mediaId, mediaDetail.type.toCore())

                listEntryResult.onSuccess { listEntry ->
                    _state.update {
                        it.copy(
                            selectedMediaDetail = mediaDetail,
                            selectedMediaListEntry = listEntry,
                            isBottomSheetVisible = true,
                            isLoadingBottomSheet = false
                        )
                    }
                }.onFailure {
                    _state.update {
                        it.copy(
                            selectedMediaDetail = mediaDetail,
                            selectedMediaListEntry = null,
                            isBottomSheetVisible = true,
                            isLoadingBottomSheet = false
                        )
                    }
                }
            }.onFailure {
                _state.update { it.copy(isLoadingBottomSheet = false) }
            }
        }
    }

    fun saveToList(
        status: MediaListStatus,
        progress: Int,
        score: Float?,
        note: String,
        startDate: String?,
        rewatches: Int,
        priority: MediaListPriority
    ) {
        viewModelScope.launch {
            val currentDetail = _state.value.selectedMediaDetail ?: return@launch
            val existingEntry = _state.value.selectedMediaListEntry

            val mediaInfo = CachedMediaInfo(
                title = currentDetail.title,
                coverImage = currentDetail.coverImageUrl ?: currentDetail.bannerImageUrl,
                totalEpisodes = null,
                totalChapters = currentDetail.chapters,
                totalVolumes = currentDetail.volumes,
                mediaStatus = currentDetail.status
            )

            val result = if (existingEntry != null) {
                updateListEntryUseCase(
                    mediaId = currentDetail.id,
                    mediaType = currentDetail.type.toCore(),
                    status = status,
                    progress = progress,
                    score = score,
                    notes = note,
                    startDate = startDate,
                    repeatCount = rewatches,
                    priority = priority,
                    mediaStatus = currentDetail.status
                )
            } else {
                addToListUseCase(
                    mediaId = currentDetail.id,
                    mediaType = currentDetail.type.toCore(),
                    status = status,
                    mediaInfo = mediaInfo
                ).onSuccess { entry ->
                    if (progress != 0 || score != null || note.isNotEmpty() || startDate != null || rewatches != 0 || priority != MediaListPriority.MEDIUM) {
                        updateListEntryUseCase(
                            mediaId = currentDetail.id,
                            mediaType = currentDetail.type.toCore(),
                            status = status,
                            progress = progress,
                            score = score,
                            notes = note,
                            startDate = startDate,
                            repeatCount = rewatches,
                            priority = priority
                        )
                    } else {
                        Result.success(entry)
                    }
                }
            }

            result.onSuccess {
                dismissBottomSheet()
            }
        }
    }

    fun dismissBottomSheet() {
        _state.update {
            it.copy(
                selectedMediaDetail = null,
                selectedMediaListEntry = null,
                isBottomSheetVisible = false,
                isLoadingBottomSheet = false
            )
        }
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

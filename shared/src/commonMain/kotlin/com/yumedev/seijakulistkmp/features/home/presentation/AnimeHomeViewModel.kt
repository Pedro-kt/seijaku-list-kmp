package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.data.remote.graphql.type.MediaSeason
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.GetAnimeDetailUseCase
import com.yumedev.seijakulistkmp.features.detail.presentation.utils.toCore
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetAnimeHomeDataUseCase
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.ErrorUiMapper
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toAnimeCardItem
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toFeaturedMediaItem
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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AnimeHomeViewModel(
    private val getAnimeHomeDataUseCase: GetAnimeHomeDataUseCase,
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase,
    private val addToListUseCase: AddToListUseCase,
    private val updateListEntryUseCase: UpdateListEntryUseCase,
    private val getListEntryUseCase: GetListEntryUseCase,
    private val mediaStringFormatter: MediaStringFormatter,
    private val errorUiMapper: ErrorUiMapper
) : ViewModel() {

    private val _state = MutableStateFlow(AnimeHomeState())
    val state: StateFlow<AnimeHomeState> = _state.asStateFlow()

    private var animeAutoScrollJob: Job? = null
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
                    isLoadingFeaturedAnime = true,
                    isLoadingAiringNow = true,
                    isLoadingNextSeason = true,
                    isLoadingTopRated = true,
                    featuredAnimeError = null,
                    airingNowError = null,
                    nextSeasonError = null,
                    topRatedError = null
                )
            }

            val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val currentMonth = currentDate.monthNumber
            val currentYear = currentDate.year

            val nextSeason = when (currentMonth) {
                in 1..3 -> MediaSeason.SPRING
                in 4..6 -> MediaSeason.SUMMER
                in 7..9 -> MediaSeason.FALL
                in 10..12 -> MediaSeason.WINTER
                else -> MediaSeason.SPRING
            }

            val nextSeasonYear = if (currentMonth >= 10) currentYear + 1 else currentYear

            val result = getAnimeHomeDataUseCase(
                nextSeason = nextSeason,
                nextSeasonYear = nextSeasonYear,
                isAdult = false
            )

            result.onSuccess { data ->
                val featuredItems = data.featured?.media?.mapNotNull { media ->
                    media?.toFeaturedMediaItem(mediaStringFormatter)
                } ?: emptyList()

                val airingNowItems = data.airingNow?.media?.mapNotNull { media ->
                    media?.toAnimeCardItem(mediaStringFormatter)
                } ?: emptyList()

                val nextSeasonItems = data.nextSeason?.media?.mapNotNull { media ->
                    media?.toAnimeCardItem(mediaStringFormatter)
                } ?: emptyList()

                val topRatedItems = data.topRated?.media?.mapNotNull { media ->
                    media?.toAnimeCardItem(mediaStringFormatter)
                } ?: emptyList()

                _state.update {
                    it.copy(
                        featuredAnime = featuredItems,
                        currentFeaturedAnimeIndex = 0,
                        airingNowAnime = airingNowItems,
                        nextSeasonAnime = nextSeasonItems,
                        topRatedAnime = topRatedItems,
                        isInitialLoading = false,
                        isLoadingFeaturedAnime = false,
                        isLoadingAiringNow = false,
                        isLoadingNextSeason = false,
                        isLoadingTopRated = false,
                        featuredAnimeError = null,
                        airingNowError = null,
                        nextSeasonError = null,
                        topRatedError = null
                    )
                }

                if (featuredItems.isNotEmpty()) {
                    startAnimeAutoScroll()
                }
            }.onFailure { error ->
                val errorType = ErrorMapper.mapToErrorType(error)
                val errorUi = errorUiMapper.mapFeaturedError(errorType)

                _state.update {
                    it.copy(
                        isInitialLoading = false,
                        isLoadingFeaturedAnime = false,
                        isLoadingAiringNow = false,
                        isLoadingNextSeason = false,
                        isLoadingTopRated = false,
                        featuredAnimeError = errorUi
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

    fun onFeaturedAnimeInteraction() {
        startAnimeAutoScroll()
    }

    fun onCardLongPress(mediaId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingBottomSheet = true) }

            val detailResult = getAnimeDetailUseCase(mediaId)

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
                totalEpisodes = currentDetail.episodes,
                totalChapters = null,
                totalVolumes = null,
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
        animeAutoScrollJob?.cancel()
    }

    fun refreshAll() {
        hasLoadedData = false
        loadAllSections()
    }

    fun retryAllAnime() {
        hasLoadedData = false
        loadAllSections()
    }
}

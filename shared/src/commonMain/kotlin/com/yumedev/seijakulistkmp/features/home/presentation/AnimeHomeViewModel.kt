package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetAnimeHomeDataUseCase
import com.yumedev.seijakulistkmp.data.remote.graphql.GetAnimeHomeDataQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.type.MediaSeason
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.ErrorUiMapper
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toAnimeCardItem
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toFeaturedMediaItem
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

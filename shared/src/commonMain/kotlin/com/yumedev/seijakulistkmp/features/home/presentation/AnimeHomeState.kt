package com.yumedev.seijakulistkmp.features.home.presentation

import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail
import com.yumedev.seijakulistkmp.features.home.presentation.model.AnimeCardItem
import com.yumedev.seijakulistkmp.features.home.presentation.model.ErrorUiModel
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry

data class AnimeHomeState(
    val featuredAnime: List<FeaturedMediaItem> = emptyList(),
    val currentFeaturedAnimeIndex: Int = 0,
    val isLoadingFeaturedAnime: Boolean = false,
    val featuredAnimeError: ErrorUiModel? = null,

    val airingNowAnime: List<AnimeCardItem> = emptyList(),
    val isLoadingAiringNow: Boolean = false,
    val airingNowError: String? = null,

    val nextSeasonAnime: List<AnimeCardItem> = emptyList(),
    val isLoadingNextSeason: Boolean = false,
    val nextSeasonError: String? = null,

    val topRatedAnime: List<AnimeCardItem> = emptyList(),
    val isLoadingTopRated: Boolean = false,
    val topRatedError: String? = null,

    val isRefreshing: Boolean = false,
    val isInitialLoading: Boolean = true,

    val selectedMediaDetail: MediaDetail? = null,
    val selectedMediaListEntry: MediaListEntry? = null,
    val isBottomSheetVisible: Boolean = false,
    val isLoadingBottomSheet: Boolean = false
) {
    val hasInitialData: Boolean
        get() = featuredAnime.isNotEmpty() &&
                airingNowAnime.isNotEmpty() &&
                nextSeasonAnime.isNotEmpty()
}
